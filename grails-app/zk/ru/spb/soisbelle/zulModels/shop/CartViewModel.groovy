package ru.spb.soisbelle.zulModels.shop

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.InputEvent
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Spinner
import ru.spb.soisbelle.CartService
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.ProductImageryWrapper

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 4/20/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
class CartViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication

  //Логгер
  static Logger log = LoggerFactory.getLogger(CartViewModel.class)

  ListModelList<ProductImageryWrapper> cartProduct

  CartService cartService //= ApplicationHolder.getApplication().getMainContext().getBean("cartService") as CartService

  @Init
  public void init(){
    cartService = grailsApplication.getMainContext().getBean("cartService")
    List<ProductImageryWrapper> models = new ArrayList<ProductImageryWrapper>()
    cartService.getCartProducts().each {it ->
      ProductImageryWrapper model = new ProductImageryWrapper(it, STD_IMAGE_SIZES.SMALL.getSize())
      cartService.initAsCartItem(model)
      models.add(model)
    }
    cartProduct = new ListModelList<ProductImageryWrapper>(models)
  }

  @Command
  @NotifyChange(["cartProduct"])
  public void removeItem(@BindingParam("productModel") ProductImageryWrapper productModel){
    try {
      cartService.removeFromCart(ProductEntity.get(productModel.id))

      List<ProductImageryWrapper> models = new ArrayList<ProductImageryWrapper>()
      cartService.getCartProducts().each {it ->
        ProductImageryWrapper model = new ProductImageryWrapper(it)
        cartService.initAsCartItem(model)
        models.add(model)
      }

      cartProduct.clear()
      cartProduct.addAll(models)

    } catch (Exception e) {
      log.debug(e.getMessage())
    }
  }

  @Command
  public void processCount(@BindingParam("productModel") ProductImageryWrapper productModel,
                           @BindingParam("inputEvent") InputEvent event){
    try {
      Long currentValue = event.value as Long
      Long previousValue = event.previousValue as Long
      if (previousValue != currentValue) {
        boolean direct = currentValue > previousValue
        long mark = direct ? 1L : -1L
        cartService.incrementCount(ProductEntity.get(productModel.id), mark)

        Long newPrice = productModel.totalPrice + (mark * productModel.price)
        productModel.setTotalPrice(newPrice)

        (event.getTarget() as Spinner).setValue(cartService.getProductCount(productModel.id) as Integer)
        BindUtils.postNotifyChange(null, null, productModel, "totalPrice");
      }
    } catch (Exception e) {
      log.debug(e.getMessage())
    }
  }

  @Command
  public void checkout(){
    Executions.sendRedirect("/shop/checkout");
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }
}
