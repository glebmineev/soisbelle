package ru.spb.soisbelle.zulModels.menu

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.AdminMenuItemEntity
import ru.spb.soisbelle.wrappers.AdminMenuItemWrapper

/**
 * Created by gleb on 17.05.14.
 */
class AdminMenuViewModel {

  List<AdminMenuItemWrapper> wrappers = new ArrayList<AdminMenuItemWrapper>()

  @Init
  public void init() {

    List<AdminMenuItemEntity> parentMenu = AdminMenuItemEntity.findAllWhere(parentMenuItem: null)

    parentMenu.each { it ->
      AdminMenuItemWrapper parentWrapper = new AdminMenuItemWrapper(it.name, it.href)
      AdminMenuItemEntity.findAllByParentMenuItem(it).each { child ->
        AdminMenuItemWrapper childWrapper = new AdminMenuItemWrapper(child.name, child.href)
        parentWrapper.addToChildren(childWrapper)
      }

      wrappers.add(parentWrapper)

    }

  }

}
