package ru.spb.soisbelle

class PromoEntity {

  String name

  static mapping = {
    table: 'promo'
    columns {
      id column: 'promo_id'
      name column: 'promo_name'
    }

    version false

  }

  static constraints = {
    name nullable: false
  }

}
