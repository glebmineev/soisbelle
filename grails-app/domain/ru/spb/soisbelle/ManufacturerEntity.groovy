package ru.spb.soisbelle

import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.core.IImagable

class ManufacturerEntity implements Domain, IImagable {

  String name
  String shortName
  String description
  String picUuid

  static mapping = {

    datasource 'ALL'

    table: 'manufacturer'
    columns {
      id column: 'manufacturer_id'
      name column: 'manufacturer_name'
      shortName column: 'manufacturer_shortname'
      description column: 'manufacturer_description'
      picUuid column: 'manufacturer_picuuid'
    }
    version false

    productList cascade: 'all-delete-orphan'
  }

  static hasMany = [
      productList: ProductEntity//,
      //categories: CategoryEntity
  ]

  static constraints = {
    name nullable: true
    shortName nullable: true
    description nullable: true
    picUuid nullable: true
  }

  public String toString() {
    return name
  }
}
