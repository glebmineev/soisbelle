package ru.spb.locon.zulModels.menu

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Init
import ru.spb.locon.CategoryEntity
import ru.spb.locon.InitService


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
