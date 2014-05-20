package ru.spb.soisbelle.zulModels.menu

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Init
import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.wrappers.CategoryWrapper


/**
 * Created by gleb on 3/2/14.
 */
class MenuViewModel {

  InitService initService = ApplicationHolder.getApplication().getMainContext().getBean("initService") as InitService
  List<CategoryWrapper> menuItems = new ArrayList<CategoryWrapper>();

  @Init
  public void init() {
    menuItems.addAll(initService.categories)
  }

}
