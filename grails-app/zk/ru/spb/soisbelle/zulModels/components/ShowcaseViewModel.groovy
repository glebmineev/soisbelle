package ru.spb.soisbelle.zulModels.components

import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.ExecutionArgParam
import org.zkoss.bind.annotation.GlobalCommand
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zhtml.Li
import org.zkoss.zhtml.Ul
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Div
import org.zkoss.zul.Include
import ru.spb.soisbelle.CartService
import ru.spb.soisbelle.ImageService
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.wrappers.ProductImageryWrapper

/**
 * Модель компонента витрины.
 */
class ShowcaseViewModel {

  static Logger log = LoggerFactory.getLogger(ShowcaseViewModel.class)

  //Все товары
  List<ProductEntity> allProducts = new ArrayList<ProductEntity>()
  //Показываемые пользователю товары
  List<ProductImageryWrapper> products = new ArrayList<ProductImageryWrapper>()

  long showToPage = 9

  //Индекс в списке всех товаров(allProducts)
  int currentIndex

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
  boolean showAppendBtn = false
  //Уникальный идентифткатор вложения.
  String uuidInclude

  @Init
  public void init(@ExecutionArgParam("allProducts") List<ProductEntity> data,
                   @ExecutionArgParam("isChangeShow") String isChangeShow,
                   @ExecutionArgParam("showAppendBtn") String showAppendBtn) {
    uuidInclude = UUID.randomUUID()
    this.isChangeShow = Boolean.parseBoolean(isChangeShow)
    this.showAppendBtn = Boolean.parseBoolean(showAppendBtn)
    isBusy = true
    currentIndex = 0;
    products.clear()
    allProducts.clear()
    allProducts.addAll(data)
  }

  /**
   * Первичная инициализация модели каталога.
   * @param data
   */
  @GlobalCommand
  @NotifyChange(["products", "isBusy", "showAppendBtn"])
  public void refreshShowcase(@BindingParam("data") List<ProductImageryWrapper> data){
    currentIndex = 0;
    products.clear()
    int dateSize = data.size()
    if (dateSize > 0) {
      currentIndex += dateSize > showToPage - 1 ? showToPage : dateSize
      products.addAll(data.subList(0, currentIndex))
    }
    this.isBusy = false
  }

  List<ProductImageryWrapper> transform(List<ProductEntity> target) {
    return Collections2.transform(target, new Function<ProductEntity, ProductImageryWrapper>() {
      @Override
      ProductImageryWrapper apply(ProductEntity f) {
        ProductImageryWrapper wrapper = new ProductImageryWrapper(f)
        cartService.initAsCartItem(wrapper)
        return wrapper;
      }
    }) as List<ProductImageryWrapper>
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

  /**
   * Добавление
   * @param event
   */
  @Command
  @NotifyChange(["showAppendBtn"])
  public void appendElse(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Include showcaseDiv = event.getTarget().getSpaceOwner() as Include
    Ul root = showcaseDiv.getFellow("products") as Ul
    addRows(root)
  }

  public void addRows(Ul parent) {
    int nextIndex = currentIndex + showToPage
    int allProductsSize = allProducts.size()
    List<ProductImageryWrapper> subList = new ArrayList<ProductImageryWrapper>()
    if (nextIndex < allProductsSize) {
      subList = transform(allProducts.subList(currentIndex, nextIndex))
      currentIndex = currentIndex + showToPage
    } else if (nextIndex > allProductsSize) {
      subList = transform(allProducts.subList(currentIndex, allProductsSize))
      currentIndex = allProductsSize
      showAppendBtn = false
    }

    subList.each { it ->
      Li li = new Li()
      Map<Object, Object> params = new HashMap<Object, Object>()
      params.put("productWrapper", it)
      Include productCell = new Include()
      productCell.setWidth("200px")
      productCell.setHeight("350px")
      productCell.setSrc("/zul/components/showcaseItem.zul")
      productCell.setDynamicProperty("productWrapper", it)
      li.appendChild(productCell)
      parent.appendChild(li)
    }

  }



}
