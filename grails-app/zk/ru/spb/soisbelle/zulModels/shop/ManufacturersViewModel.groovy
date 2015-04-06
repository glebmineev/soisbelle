package ru.spb.soisbelle.zulModels.shop

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.ExecutionArgParam
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Page
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.Div
import org.zkoss.zul.Include
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.HrefWrapper
import ru.spb.soisbelle.wrappers.ManufacturerWrapper

/**
 * Created by gleb on 14.05.14.
 */
class ManufacturersViewModel {

  ManufacturerWrapper manufacturerWrapper
  List<ProductEntity> products = new ArrayList<>()
  ListModelList<String> countPageItemModel
  //Навигация.
  List<HrefWrapper> links = new LinkedList<HrefWrapper>()
  boolean pageIsLoad = false

  @Init
  public void init() {
    Long manufacturer_id = Executions.getCurrent().getParameter("manufacturer") as Long
    ManufacturerEntity manufacturer = ManufacturerEntity.get(manufacturer_id)

    manufacturerWrapper = new ManufacturerWrapper(manufacturer);
    products.addAll(manufacturer.productList)

    List<String> model = new ArrayList<String>();
    model.add("8");
    model.add("16");
    model.add("24");

    countPageItemModel = new ListModelList<String>(model)
    countPageItemModel.addToSelection(model.get(0));

    initLinks()

  }

  void initLinks(){
    links.add(new HrefWrapper("Главная", "/shop"))
    links.add(new HrefWrapper(manufacturerWrapper.name, "/shop/manufacturers?manufacturer=${manufacturerWrapper.id}"))
  }

  @Command
  public void refreshWhenBackToCatalog(@BindingParam("div") Div self){
    if (pageIsLoad) {
      Component showcase = self.getFellow("showcase")
      showcase.removeChild(showcase.getFellow("showcase-div"))
      self.invalidate()
    }
    pageIsLoad = true

  }

  @Command
  public void applyRowTemplate(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Page page = event.getTarget().getSpaceOwner() as Page
    Div productsDiv = page.getFellow("showcase").getFellow("showcase-div") as Div
    productsDiv.setSclass("products-row-template")
  }


  @Command
  public void applyCellTemplate(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Page page = event.getTarget().getSpaceOwner() as Page
    Div productsDiv = page.getFellow("showcase").getFellow("showcase-div") as Div
    productsDiv.setSclass("products-cell-template")
  }

  /**
   * Изменение количества видимых компонентов.
   */
  @Command
  public void changeShowItems(){


    Set<String> selection = countPageItemModel.getSelection()

    Page page = ExecutionsCtrl.getCurrentCtrl().getCurrentPage()
    Include showcase = page.getFellow("showcase") as Include
    showcase.setDynamicProperty("limit", selection.first() as Long)
    showcase.invalidate()

  }

}
