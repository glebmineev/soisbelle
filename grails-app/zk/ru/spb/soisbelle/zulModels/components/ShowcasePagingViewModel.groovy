package ru.spb.soisbelle.zulModels.components

import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.*
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Div
import org.zkoss.zul.Include
import ru.spb.soisbelle.*
import ru.spb.soisbelle.wrappers.ProductWrapper

/**
 * Модель компонента витрины.
 */
class ShowcasePagingViewModel {

  static Logger log = LoggerFactory.getLogger(ShowcasePagingViewModel.class)
  //Все товары
  List<ProductEntity> allProducts = new ArrayList<ProductEntity>()
  //Показываемые пользователю товары
  List<ProductWrapper> products = new ArrayList<ProductWrapper>()

  /**
   * Сколько отображать на странице
   */
  int showToPage

  /**
   * Шаг
   */
  int step

  //Индекс в списке всех товаров(allProducts)
  int currentPos

  /**
   * Показывать ли окно загрузки.
   */
  boolean isBusy

  /**
   * Необходимые сервисы.
   */
  ImageService imageService = ApplicationHolder.getApplication().getMainContext().getBean("imageService") as ImageService
  CartService cartService = ApplicationHolder.getApplication().getMainContext().getBean("cartService") as CartService
  InitService initService = ApplicationHolder.getApplication().getMainContext().getBean("initService") as InitService
  ServerFoldersService serverFoldersService =
    ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  //Id выбранной пользователем категории.
  Long categoryID

  /**
   * Параметры для настройки вложения.
   */
  //Показывать или нет панель изменения вида отображения витрины.
  boolean isChangeShow = false
  //Показать или нет кнопку добавить.
  boolean endList = false
  //Уникальный идентифткатор вложения.
  String uuidInclude

  @Init
  public void init(@ExecutionArgParam("allProducts") List<ProductEntity> data,
                   @ExecutionArgParam("isChangeShow") String isChangeShow,
                   @ExecutionArgParam("countPageItems") Long countPageItems) {
    uuidInclude = UUID.randomUUID()
    this.showToPage = countPageItems
    this.step = countPageItems
    this.isChangeShow = Boolean.parseBoolean(isChangeShow)
    isBusy = true
    currentPos = 0;
    products.clear()
    allProducts.clear()
    allProducts.addAll(data)
  }

  /**
   * Первичная инициализация модели каталога.
   * @param data
   */
  @GlobalCommand
  @NotifyChange(["products", "isBusy", "currentPos"])
  public void refreshShowcase(@BindingParam("data") List<ProductWrapper> data){
    this.currentPos = 0;
    products.clear()
    int dateSize = data.size()
    if (dateSize > 0) {
      currentPos += dateSize > showToPage - 1 ? showToPage : dateSize
      products.addAll(data.subList(0, currentPos))
    }
    this.isBusy = false
  }

  List<ProductWrapper> transform(List<ProductEntity> target) {
    return Collections2.transform(target, new Function<ProductEntity, ProductWrapper>() {
      @Override
      ProductWrapper apply(ProductEntity f) {
        ProductWrapper wrapper = new ProductWrapper(f)
        cartService.initAsCartItem(wrapper)
        return wrapper;
      }
    }) as List<ProductWrapper>
  }

  @Command
  public void applyRowTemplate(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Include include = event.getTarget().getSpaceOwner() as Include
    Div productsDiv = include.getFirstChild().getFirstChild() as Div
    productsDiv.setSclass("products-row-template")
  }

  @Command
  public void applyCellTemplate(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Include include = event.getTarget().getSpaceOwner() as Include
    Div productsDiv = include.getFirstChild().getFirstChild() as Div
    productsDiv.setSclass("products-cell-template")
  }

  @Command
  @NotifyChange(["products", "currentPos", "endList"])
  public void next(){
    int oldPos = currentPos
    currentPos = currentPos + step
    int diff = allProducts.size() - oldPos
    if (diff <= step) {
      endList = false
      moveCarousel(oldPos, oldPos + diff)
    }
    else
    {
      moveCarousel(oldPos, currentPos)
    }
  }

  @Command
  @NotifyChange(["products", "endList", "currentPos"])
  public void back(){
    currentPos = currentPos - step
    endList = false
    moveCarousel(currentPos - step, currentPos)
  }

  void moveCarousel(int start, int end){
    products.clear()
    allProducts.subList(start, end).each {it ->
      products.add(new ProductWrapper(it))
    }
  }

}
