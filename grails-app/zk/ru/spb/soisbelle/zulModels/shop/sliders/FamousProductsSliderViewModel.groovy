package ru.spb.soisbelle.zulModels.shop.sliders

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.CartService
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.wrappers.ProductWrapper
import ru.spb.soisbelle.wrappers.Wrapper
import ru.spb.soisbelle.zulModels.core.SliderViewModel

/**
 * Created by gleb on 5/5/14.
 */
@Init(superclass=true)
class FamousProductsSliderViewModel extends SliderViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication

  InitService initService = ApplicationHolder.getApplication().getMainContext().getBean("initService") as InitService
  CartService cartService //= ApplicationHolder.getApplication().getMainContext().getBean("cartService") as CartService
  ServerFoldersService serverFoldersService =
      ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  AImage cartImage

  @Override
  void prepare() {
    cartImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("dsn")
        .appendPath("recommended")
        .appendString("toCart.png")
        .build())

    entities = new ArrayList<ProductEntity>(initService.recommended)
    cartService = grailsApplication.getMainContext().getBean("cartService")
  }

  @Override
  void setCountVisibleElement() {
    this.showToPage = 4
  }

  @Override
  Wrapper transformEntity(Domain entity) {
    ProductWrapper wrapper = new ProductWrapper(entity)
    cartService.initAsCartItem(wrapper)
    return wrapper
  }

  @Command
  public void redirectToProductItem(@BindingParam("productModel") ProductWrapper productModel){
    Executions.sendRedirect("/shop/product?product=${productModel.id}")
  }

  @Command
  public void addToCart(@BindingParam("productModel") ProductWrapper productModel){
    productModel.setInCart(true)
    cartService.addToCart(ProductEntity.get(productModel.id))
    BindUtils.postNotifyChange(null, null, productModel, "inCart");
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
