package ru.spb.soisbelle.zulModels.components

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.*
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Page
import org.zkoss.zul.Div
import org.zkoss.zul.impl.XulElement
import ru.spb.soisbelle.*
import ru.spb.soisbelle.wrappers.PageWrapper
import ru.spb.soisbelle.wrappers.ProductImageryWrapper

import java.math.RoundingMode

/**
 * Модель компонента витрины.
 */
@Deprecated
class ShowcasePagingViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication

  static Logger log = LoggerFactory.getLogger(ShowcasePagingViewModel.class)
  //Все товары
  List<ProductEntity> allProducts = new ArrayList<ProductEntity>()
  //Показываемые пользователю товары
  List<ProductImageryWrapper> products = new ArrayList<ProductImageryWrapper>()
  // Шаг
  int step
  //Индекс в списке всех товаров(allProducts)
  int currentPos
  // Показывать ли окно загрузки.
  boolean isBusy
  //Показывать или нет панель изменения вида отображения витрины.
  boolean isChangeShow = false
  //Показать или нет кнопку добавить.
  boolean endList = false
  //Показать или нет кнопку добавить.
  boolean startList = false

  boolean endPage = false
  boolean startPage = false
  int currentPage = 1
  int firstPage = 1
  int firstPos

  //Количество страниц
  int totalCount

  //перемещения внутри трехстраничного набора.
  boolean inPaging = false

  /**
   * Необходимые сервисы.
   */
  CartService cartService
  InitService initService
  ImageStorageService imageStorageService

  AImage nextImage
  AImage backImage
  AImage skipToStartImage
  AImage skipToEndImage
  AImage progressRoll

  List<PageWrapper> numberPages = new ArrayList<PageWrapper>()

  @Init
  public void init(@ExecutionArgParam("allProducts") List<ProductEntity> data,
                   @ExecutionArgParam("isChangeShow") String isChangeShow,
                   @ExecutionArgParam("countPageItems") Long countPageItems) {

    this.nextImage = imageStorageService.getNextImage()
    this.backImage = imageStorageService.getBackImage()
    this.skipToStartImage = imageStorageService.getSkipToStart()
    this.skipToEndImage = imageStorageService.getSkipToEnd()
    this.progressRoll = imageStorageService.getProgressRoll()

    this.step = countPageItems
    this.isChangeShow = Boolean.parseBoolean(isChangeShow)
    this.isBusy = true
    this.currentPos = 0
    this.firstPos = 0

    this.products.clear()
    this.allProducts.clear()
    this.allProducts.addAll(data)

    inPaging = true

    //allProducts.sort {a,b ->
    //  a<=>b
    //}

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
  @NotifyChange(["products", "isBusy", "currentPos", "numberPages", "totalCount", "endPage", "startPage", "startList", "endList"])
  public void refreshShowcase(@BindingParam("data") List<ProductImageryWrapper> data) {
    this.currentPos = 0;
    products.clear()
    int dateSize = data.size()

    if(dateSize == 0){
      this.startPage = true
      this.endPage = true
    }

    if (dateSize > 0) {
      currentPos += dateSize > step - 1 ? step : dateSize
      this.firstPos = currentPos
      products.addAll(data.subList(0, currentPos))
        this.startList = true
        this.startPage = true
        numberPages.clear()

        Float value = allProducts.size() / step
        totalCount = new BigDecimal(value).setScale(0, RoundingMode.UP).intValue()

        if (totalCount > 3/*allProducts.size() <= step*/){
          fillNumberPages(1)
        } else {
          fillNumberPages(1, totalCount)
          this.endPage = true
          this.endList = true

        }


        int visibleNextPoints = totalCount - currentPage
        if (visibleNextPoints <= 3) {
          endPage = true
        }

      }

    this.isBusy = false
  }

  /**
   * Страницы
   * @param dataSize - размер данных.
   */
  void fillNumberPages(int startPage) {
    int countPages = 3

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
    firstPage = first?.number
    currentPage = firstPage

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
    firstPage = first?.number
    currentPage = firstPage

  }

  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "totalCount", "startList", "endList"])
  public void moveToPage(@BindingParam("pageNumber") PageWrapper pageWrapper) {

    inPaging = true

    int page = pageWrapper.number
    int diff = Math.abs((currentPage - page))

    if (page > currentPage) {
      while (diff > 0) {
        next()
        diff--
      }
    } else if (page < currentPage) {
      while (diff > 0) {
        prev()
        diff--
      }
    }

    selectPage(pageWrapper)

    currentPage = page

    long isLittleList = (long) (totalCount / 3)

    //Если число страниц 5
    if (isLittleList < 2 && isLittleList >= 1) {
      int visibleStartPoints = totalCount - firstPage
      if (visibleStartPoints >= 3){
        endList = false
        startList = true
      } else {
        endList = true
        startList = false
      }
    } else {
      //Если число стрниц 3
      endList = true
      startList = true
    }

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
   * Прокрутка на 3 страницы вперед
   */
  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "endPage", "startPage", "startList", "endList"])
  public void nextMore() {

    inPaging = false

    int pageStep = 3
    int lastPage = firstPage + 3
    int d = (totalCount - lastPage)
    numberPages.clear()

    if (d > 2) {
      fillNumberPages(lastPage)
      moveNext(pageStep)
      //endPage = false
      startPage = false
    } else {
      //если уже конец списка - не двигаемся дальше
      if (endPage){
        return
      }

      int diff = (totalCount - firstPage);
      lastPage = totalCount - 2
      fillNumberPages(lastPage)
      moveNext(diff - 2)

      endPage = true
      startPage = false
    }
  }

  /**
   * Прокрутка на 3 страницы назад
   */
  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "endPage", "startPage", "startList", "endList"])
  public void prevMore() {

    inPaging = false

    int pageStep = 3
    numberPages.clear()
    int lastPage = firstPage - 3

    int d = (totalCount - lastPage)
    if (d < totalCount) {
      fillNumberPages(lastPage)
      movePrev(pageStep)
      endPage = false
    } else {

      if (startPage)
        return

      lastPage = totalCount - (totalCount - 1)
      fillNumberPages(lastPage)
      //TODO:: подумать правильно ли magic number
      movePrev(2)
      endPage = false
      startPage = true
    }
  }

  /**
   * Прокрутка на 1 страницу вперед
   */
  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "endPage", "startPage", "startList", "endList"])
  public void nextOnePage() {
    inPaging = false
    nextPage()
    setCurrentPos()
    next()
    setFirstPos()
  }

  /**
   * Прокрутка на 1 страницу назад
   */
  @Command
  @NotifyChange(["products", "currentPos", "numberPages", "endPage", "startPage", "startList", "endList"])
  public void prevOnePage() {
    inPaging = false
    prevPage()
    setCurrentPos()
    prev()
    setFirstPos()
  }

  //TODO:: переделать чобы без данных работало
  public int setCurrentPos() {
    int append = 0
    currentPos = firstPos
    if (currentPos != firstPos) {
      int count = Math.abs(currentPos - firstPos)
      while (count > 0) {
        append++
        prev()
      }
    } /*else {
      currentPos = currentPos + step
    }*/
    return append

  }

  public void setFirstPos() {
    firstPos = currentPos
  }

  private void next() {
    int oldPos = currentPos
    currentPos = currentPos + step
    int diff = allProducts.size() - oldPos
    if (diff <= step) {
      //endPage = true
      //startPage = false
      if (totalCount > 3) {
        endList = true
        startList = false
      }
      moveCarousel(oldPos, oldPos + diff)
    } else {
      if (totalCount > 3) {
        startPage = false
        startList = false
      }
      moveCarousel(oldPos, currentPos)
    }

    int visibleEndPoints = totalCount - currentPage
    if (visibleEndPoints <= 3){
      endPage = true
    }

    if (visibleEndPoints < 3){
      endList = true
    }

    int visibleStartPoints = currentPage - 3
    if (visibleStartPoints < 0) {
      startPage = true
    }

    //Если внутри страниц и недалеко от начала, то не отображаем ...
    if (inPaging && visibleStartPoints < 0){
      startList = true
    }

  }

  private void prev() {
    currentPos = currentPos - step
    if (currentPos == step) {
      startList = true
      startPage = true
    }
    if (totalCount > 3) {
      endPage = false
      endList = false
    }

    if (currentPage < 3) {
      startPage = true
    }

    int visibleNextPoints = totalCount - currentPage
    if (visibleNextPoints <= 3) {
      endPage = true
    }

    moveCarousel(currentPos - step, currentPos)
  }

  void moveCarousel(int start, int end) {
    products.clear()
    allProducts.subList(start, end).each { it ->
      ProductImageryWrapper wrapper = new ProductImageryWrapper(it)
      boolean inCart = cartService.findedInCart(wrapper.id)
      wrapper.setInCart(inCart)
      products.add(wrapper)
    }
  }

  public void nextPage() {
    int nextPage = firstPage + 1

    if (nextPage >= (totalCount - 2)) {
      this.endPage = true
      this.endList = true
      nextPage = totalCount - 2
    }

    numberPages.clear()
    fillNumberPages(nextPage)
  }

  public void prevPage() {
    int prevPage = firstPage - 1
    if (prevPage == 1) {
      this.startList = true
      this.startPage = true
    }

    numberPages.clear()
    if (totalCount < 3){
      endList = true
      endPage = true
      fillNumberPages(1, totalCount)
    } else {
      fillNumberPages(prevPage)
    }

  }

  public void movePrev(int countMoved){
    int pos = setCurrentPos()
    //countMoved = countMoved + pos
    while (countMoved > 0) {
      prev()
      countMoved--
    }
    setFirstPos()
  }

  public void moveNext(int countMoved){
    int pos = setCurrentPos()
    //countMoved = countMoved - pos
    while (countMoved > 0) {
      next()
      countMoved--
    }
    setFirstPos()
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
