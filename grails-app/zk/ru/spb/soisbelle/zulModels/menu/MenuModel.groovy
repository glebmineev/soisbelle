package ru.spb.soisbelle.zulModels.menu

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Init
import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.InitService


/**
 * Created by gleb on 3/2/14.
 */
class MenuModel {

  InitService initService = ApplicationHolder.getApplication().getMainContext().getBean("initService") as InitService
  List<CategoryEntity> menuItems = new ArrayList<CategoryEntity>();

  @Init
  public void init() {
    menuItems.addAll(initService.categories)
  }

}
