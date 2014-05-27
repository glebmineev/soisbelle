package ru.spb.soisbelle

class InfoEntity {

  String name
  String href
  String description

  static mapping = {
    table: 'info'
    columns {
      id column: 'info_id'
      description column: 'info_description'
      href column: 'info_href'
      name column: 'info_name'
    }

    version false
  }

  static constraints = {
    description nullable: true, maxSize: 65535
    name nullable: true, maxSize: 65535
    href nullable: true, maxSize: 65535
  }

}
