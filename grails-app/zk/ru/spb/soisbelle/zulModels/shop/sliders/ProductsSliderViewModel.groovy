package ru.spb.soisbelle.zulModels.shop.sliders

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.wrappers.ProductWrapper
import ru.spb.soisbelle.wrappers.Wrapper
import ru.spb.soisbelle.zulModels.core.SliderViewModel

/**
 * Created by gleb on 14.05.14.
 */
@Init(superclass=true)
class ProductsSliderViewModel extends SliderViewModel {

  @Override
  void prepare() {
    entities = Executions.getCurrent().getArg().get("products")
  }

  @Override
  void setCountVisibleElement() {
    this.showToPage = 4
  }

  @Override
  Wrapper transformEntity(Domain entity) {
    ProductWrapper wrapper = new ProductWrapper(entity)
    return wrapper
  }

  @Command
  public void redirectToProductItem(@BindingParam("productModel") ProductWrapper productModel){
    Executions.sendRedirect("/shop/product?product=${productModel.id}")
  }

}
