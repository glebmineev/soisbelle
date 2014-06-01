package ru.spb.soisbelle.common

enum STD_FILE_NAMES {

  PRODUCT_NAME("1"), CATEGORY_NAME("1"), MANUFACTURER_NAME("1"), USER_NAME("1"), EMPTY_IMAGE("empty.png"), PROMO_NAME("1")

  private String name

  STD_FILE_NAMES(String name) {
    this.name = name
  }

  String getName() {
    return name
  }

  void setName(String name) {
    this.name = name
  }

}
