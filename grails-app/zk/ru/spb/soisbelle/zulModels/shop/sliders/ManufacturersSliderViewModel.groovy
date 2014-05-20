package ru.spb.soisbelle.zulModels.shop.sliders

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.wrappers.ManufacturerWrapper
import ru.spb.soisbelle.wrappers.Wrapper
import ru.spb.soisbelle.zulModels.core.SliderViewModel

/**
 * Created by gleb on 5/5/14.
 */

@Init(superclass=true)
class ManufacturersSliderViewModel extends SliderViewModel {

  @Override
  void prepare() {
    entities = ManufacturerEntity.list(sort: "name")
  }

  @Override
  void setCountVisibleElement() {
    this.showToPage = 6
  }

  @Override
  Wrapper transformEntity(Domain entity) {
    return new ManufacturerWrapper(entity as ManufacturerEntity)
  }

  @Command
  public void redirectToManufacturers(@BindingParam("wrapper") ManufacturerWrapper wrapper) {
    Executions.sendRedirect("/shop/manufacturers?manufacturer=${wrapper.id}")
  }

}
