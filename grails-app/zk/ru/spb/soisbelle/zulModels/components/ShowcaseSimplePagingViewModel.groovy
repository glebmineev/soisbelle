package ru.spb.soisbelle.zulModels.components

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.*
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Page
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.Div
import org.zkoss.zul.Include
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.CartService
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.showcase.ArrayChunkUtils
import ru.spb.soisbelle.wrappers.PageWrapper
import ru.spb.soisbelle.wrappers.ProductWrapper

import java.math.RoundingMode

/**
 * Модель компонента витрины.
 */
class ShowcaseSimplePagingViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication

  static Logger log = LoggerFactory.getLogger(ShowcaseSimplePagingViewModel.class)
  //Все товары
  List<ProductEntity> allProducts = new ArrayList<ProductEntity>()
  //Показываемые пользователю товары
  List<ProductWrapper> products = new ArrayList<ProductWrapper>()
  // Показывать ли окно загрузки.
  boolean isBusy
  //Показывать или нет панель изменения вида отображения витрины.
  boolean isChangeShow = false

  //Если дошли до конца списка, то true
  boolean endList = false
  //Если дошли до начала списка, то true
  boolean startList = false

  //Количество элементов на странице
  int limit
  //текущее смещение в массиве
  int offset
  //текущая начальная страница
  int currentStartPage
  //Количество страниц
  int linkLimit
  //Количество страниц
  int totalCount

  long[][] allPages

  //стиль списка - широкие ячейки или узкие
  String listType

  //показывать ли пейджинг.
  boolean showPaging
  //показывать ли панель опций отображения
  boolean showOptionsPanel

  /**
   * Необходимые сервисы.
   */
  CartService cartService
  InitService initService
  ImageStorageService imageStorageService

  //Всяческие картинки
  AImage nextImage
  AImage backImage
  AImage skipToStartImage
  AImage skipToEndImage

  ListModelList<String> countPageItemModel

  List<PageWrapper> numberPages = new ArrayList<PageWrapper>()

  @Init
  public void init(@ExecutionArgParam("allProducts") List<ProductEntity> data,
                   @ExecutionArgParam("isChangeShow") String isChangeShow,
                   @ExecutionArgParam("listType") String listType,
                   @ExecutionArgParam("showPaging") String showPaging,
                   @ExecutionArgParam("showOptionsPanel") String showOptionsPanel,
                   @ExecutionArgParam("linkLimit") String linkLimit,
                   @ExecutionArgParam("limit") String limit) {

    //TODO :: В зависимости от наличия categoryId переключать режим слайдера.

    this.nextImage = imageStorageService.getNextImage()
    this.backImage = imageStorageService.getBackImage()
    this.skipToStartImage = imageStorageService.getSkipToStart()
    this.skipToEndImage = imageStorageService.getSkipToEnd()

    this.isChangeShow = Boolean.parseBoolean(isChangeShow)
    this.isBusy = true

    this.products.clear()
    this.allProducts.clear()
    this.allProducts.addAll(data)
    this.linkLimit = linkLimit as Long
    this.limit = limit as Integer
    this.listType = listType
    this.showPaging = Boolean.parseBoolean(showPaging)
    this.showOptionsPanel = Boolean.parseBoolean(showOptionsPanel)

    initServices()
  }

  public void initServices(){
    cartService = grailsApplication.getMainContext().getBean("cartService")
    initService = grailsApplication.getMainContext().getBean("initService")
    imageStorageService = grailsApplication.getMainContext().getBean("imageStorageService")
  }

  /**
   * Первичная инициализация модели каталога.
   * @param data
   */
  @GlobalCommand
  @NotifyChange(["products", "isBusy", "numberPages", "totalCount", "startList", "endList", "showPaging", "showOptionsPanel", "countPageItemModel"])
  public void refreshShowcase(@BindingParam("data") List<ProductWrapper> data) {
    products.clear()
    int dateSize = data.size()
    offset = 0
    if (dateSize > 0) {
      int to = dateSize > limit - 1 ? limit : dateSize
      products.addAll(data.subList(0, to))

      this.startList = true
      numberPages.clear()

      Float value = allProducts.size() / limit
      totalCount = new BigDecimal(value).setScale(0, RoundingMode.UP).intValue()

      if (totalCount > linkLimit){
        fillNumberPages(1)
      } else {
        fillNumberPages(1, totalCount)
        this.endList = true
      }

      fillPageArr()

    }

    if (allProducts.size() == null) {
      this.showPaging = false
      this.showOptionsPanel = false
    }

    this.isBusy = false
  }

  /**
   * Страницы
   * @param dataSize - размер данных.
   */
  void fillNumberPages(int startPage) {
    int temp = linkLimit
    while (temp > 0) {
      int index = startPage++
      numberPages.add(new PageWrapper(index, false))
      temp--
    }

    numberPages.sort { it ->
      it.number
    }

    PageWrapper first = numberPages.first()
    first.selected = true

  }

  /**
   * Заполнение массива страниц.
   * @return
   */
  public fillPageArr() {
    long[] pagesArr = new long[totalCount]
    for(int i = 1; i < totalCount; i++) {
      pagesArr[i] = i * limit;
    }

    allPages = ArrayChunkUtils.chunkArray(pagesArr, linkLimit)

  }

  /**
   * Страницы
   * @param dataSize - размер данных.
   */
  void fillNumberPages(int startPage, int countPages) {

    while (countPages > 0) {
      int index = startPage++
      numberPages.add(new PageWrapper(index, false))
      countPages--
    }

    numberPages.sort { it ->
      it.number
    }

    PageWrapper first = numberPages.first()
    first.selected = true
    //currentPage = first?.number

  }


  //Переход на страницу
  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "totalCount", "startList", "endList"])
  public void moveToPage(@BindingParam("pageNumber") PageWrapper pageWrapper) {

    int number = pageWrapper.getNumber()

    long[] currentPages = allPages[offset];
    int from = ((int) currentPages[0] / limit)
    int diff = (number - (from + 1))
    if (number != totalCount) {
      moveProductList((int) currentPages[diff], (int) currentPages[diff] + limit)
    } else {
      moveProductList((int) currentPages[diff], allProducts.size())
    }

    selectPage(pageWrapper)

  }

  public void selectPage(PageWrapper wrapper) {
    for (PageWrapper pw : numberPages) {
      pw.selected = false
      BindUtils.postNotifyChange(null, null, pw, "selected")
    }
    wrapper.selected = !wrapper.selected
    BindUtils.postNotifyChange(null, null, wrapper, "selected")
  }

  /**
   * Прокрутка на 1 страницу вперед
   */
  @Command
  @NotifyChange(["products", "numberPages", "startList", "endList"])
  public void nextOnePage() {
    numberPages.clear()
    offset++
    movePageNumbers()

    long[] currentPages = allPages[offset];

    int st = currentPages[0]
    //Если последняя страница всего одна и товаров на ней меньше чем limit, то надо расчитать остаток.
    int ed = currentPages.length == 1 ? ((int) currentPages[0] + (allProducts.size() - ((int) currentPages[0]))) : (int) currentPages[1]

    moveProductList(st, ed)

    //видимость стрелок вперед назад.
    if (offset == ( allPages.length - 1 )) {
      endList = true
    }

    startList = false

  }

  /**
   * Прокрутка на 1 страницу назад
   */
  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "startList", "endList"])
  public void prevOnePage() {
    numberPages.clear()
    offset--
    movePageNumbers()

    long[] currentPages = allPages[offset];
    moveProductList((int) currentPages[0], (int) currentPages[1])

    //видимость стрелок вперед назад.
    if (offset == 0) {
      startList = true
    }

    endList = false

  }

  void movePageNumbers() {
    long[] currentPages = allPages[offset];
    int from = ((int) currentPages[0] / limit)
    int to = ((int) currentPages[currentPages.length - 1] / limit) + 1
    fillNumberPages(from + 1, (to - from))
  }

  void moveProductList(int start, int end) {
    products.clear()
    allProducts.subList(start, end).each { it ->
      ProductWrapper wrapper = new ProductWrapper(it)
      boolean inCart = cartService.findedInCart(wrapper.id)
      wrapper.setInCart(inCart)
      products.add(wrapper)
    }
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
