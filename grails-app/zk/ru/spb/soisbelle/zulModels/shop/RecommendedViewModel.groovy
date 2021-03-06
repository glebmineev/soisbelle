package ru.spb.soisbelle.zulModels.shop

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.*
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.wrappers.ProductImageryWrapper

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 4/26/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
class RecommendedViewModel {

  ListModelList<ProductImageryWrapper> products;

  InitService initService = ApplicationHolder.getApplication().getMainContext().getBean("initService") as InitService
  CartService cartService = ApplicationHolder.getApplication().getMainContext().getBean("cartService") as CartService
  ServerFoldersService serverFoldersService =
      ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  AImage cartImage

  @Init
  public void init(){
    cartImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("dsn")
        .appendPath("recommended")
        .appendString("toCart.png")
        .build())
    products = new ListModelList<ProductImageryWrapper>()
    initService.recommended.each {it ->
      ProductImageryWrapper model = new ProductImageryWrapper(it)
      cartService.initAsCartItem(model)
      products.add(model)
    }
  }

  @Command
  public void redirectToProductItem(@BindingParam("productModel") ProductImageryWrapper productModel){
    Executions.sendRedirect("/shop/product?product=${productModel.id}")
  }

  @Command
  public void addToCart(@BindingParam("productModel") ProductImageryWrapper productModel){
    productModel.setInCart(true)
    cartService.addToCart(ProductEntity.get(productModel.id))
    BindUtils.postNotifyChange(null, null, productModel, "inCart");
  }

}
