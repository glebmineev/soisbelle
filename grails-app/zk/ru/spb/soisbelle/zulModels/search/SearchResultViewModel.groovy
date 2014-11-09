package ru.spb.soisbelle.zulModels.search

import com.google.common.base.Strings
import org.apache.commons.lang.StringEscapeUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Sessions
import org.zkoss.zul.Div
import org.zkoss.zul.ListModelList
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
class SearchResultViewModel {

  ListModelList<ProductWrapper> model

  List<ProductEntity> matchedProduct = new ArrayList<ProductEntity>()

  @Init
  public void init(){
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
  }

  @Command
  public void redirectToProductItem(@BindingParam("product") ProductWrapper product){
    Executions.sendRedirect("/shop/product?product=${product.id}")
  }

  @Command
  public void refreshWhenBackToSearchResults(@BindingParam("div") Div self) {
    self.invalidate()
  }

}
