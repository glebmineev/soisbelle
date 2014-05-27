package ru.spb.soisbelle.wrappers

/**
 * Created by gleb on 17.05.14.
 */
class AdminMenuItemWrapper {

  String name
  String href

  List<AdminMenuItemWrapper> children = new ArrayList<AdminMenuItemWrapper>()

  AdminMenuItemWrapper(String name, String href) {
    this.name = name
    this.href = href
  }

  public void addToChildren(AdminMenuItemWrapper item) {
    children.add(item)
  }

}
