package ru.spb.soisbelle

/**
 * Created by gleb on 17.05.14.
 */
class AdminMenuItemEntity {

  String name
  String href

  AdminMenuItemEntity parentMenuItem

  static mapping = {
    table: 'adminmenuitem'
    columns {
      id column: 'adminmenuitem_id'
      name column: 'adminmenuitem_name'
      href column: 'adminmenuitem_href'
    }

    version false

    parentMenuItem sort: "name", order: "desc", cascade: 'all-delete-orphan'

  }

  static constraints = {
    name nullable: true, maxSize: 65535
    href nullable: true, maxSize: 65535
  }

  static hasMany = [
    parentMenuItem: AdminMenuItemEntity
  ]

}
