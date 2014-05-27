package ru.spb.soisbelle.zulModels.shop

import org.zkoss.bind.annotation.Init
import ru.spb.soisbelle.InfoEntity
import ru.spb.soisbelle.wrappers.AdminMenuItemWrapper

/**
 * Created by gleb on 25.05.14.
 */
class FooterViewModel {

  List<AdminMenuItemWrapper> menuItems = new ArrayList<AdminMenuItemWrapper>();

  @Init
  public void init() {

    InfoEntity.list(sort: "name").each {it ->
      menuItems.add(new AdminMenuItemWrapper(it.name, it.href))
    }

  }

}
