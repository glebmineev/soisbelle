package ru.spb.soisbelle.zulModels.search

import com.google.common.base.Strings
import org.apache.commons.lang.StringEscapeUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Page
import org.zkoss.zk.ui.Sessions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.Div
import org.zkoss.zul.Include
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.ProductWrapper

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 4/28/13
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */
class SearchResultViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication
  ImageStorageService imageStorageService

  AImage rows
  AImage cells

  List<ProductEntity> matchedProduct = new ArrayList<ProductEntity>()
  ListModelList<ProductWrapper> model
  ListModelList<String> countPageItemModel

  @Init
  public void init(){

    initServices()

    rows = imageStorageService.rows
    cells = imageStorageService.cells

    //String keyword = Executions.getCurrent().getParameter("keyword")
    String keyword = (String) Sessions.getCurrent().getAttribute("keyword")
    if (Strings.isNullOrEmpty(keyword))
      return

    String sql = StringEscapeUtils.escapeSql(keyword);

    String replace = sql.replace("%", "\\%");

    ManufacturerEntity manufacturer = ManufacturerEntity.findByName("${replace}");

    List<ProductEntity> list = ProductEntity.createCriteria().list {
      or {
        ilike("name", "%${replace}%")
        ilike("description", "%${replace}%")
        if (manufacturer != null) {
          sqlRestriction("product_manufacturer_id = ${manufacturer.id}")
        }

      }
    }
    matchedProduct.addAll(list)
    model = new ListModelList<ProductWrapper>();
    Sessions.getCurrent().removeAttribute("keyword")

    List<String> model = new ArrayList<String>();
    model.add("8");
    model.add("16");
    model.add("24");

    countPageItemModel = new ListModelList<String>(model)
    countPageItemModel.addToSelection(model.get(0));

  }

  public void initServices(){
    imageStorageService = grailsApplication.getMainContext().getBean("imageStorageService")
  }

  @Command
  public void redirectToProductItem(@BindingParam("product") ProductWrapper product){
    Executions.sendRedirect("/shop/product?product=${product.id}")
  }

  @Command
  public void refreshWhenBackToSearchResults(@BindingParam("div") Div self) {
    self.invalidate()
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

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
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
