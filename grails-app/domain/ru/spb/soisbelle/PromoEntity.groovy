package ru.spb.soisbelle

class PromoEntity {

  String name
  String picUuid

  static mapping = {

    datasource 'ALL'

    table: 'promo'
    columns {
      id column: 'promo_id'
      name column: 'promo_name'
      picUuid column: 'promo_picuuid'
    }

    version false

  }

  static constraints = {
    name nullable: false
    picUuid nullable: true
  }

}
