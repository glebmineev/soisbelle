package ru.spb.soisbelle.zulModels.search

import com.google.common.base.Strings
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.GlobalCommand
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Sessions
import org.zkoss.zul.ListModelList
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
    List<ProductEntity> list = ProductEntity.createCriteria().list {
      ilike("name", "%${keyword}%")
      ilike("description", "%${keyword}%")
    }
    matchedProduct.addAll(list)
    model = new ListModelList<ProductWrapper>();
    Sessions.getCurrent().removeAttribute("keyword")
  }

  @Command
  public void redirectToProductItem(@BindingParam("product") ProductWrapper product){
    Executions.sendRedirect("/shop/product?product=${product.id}")
  }

}