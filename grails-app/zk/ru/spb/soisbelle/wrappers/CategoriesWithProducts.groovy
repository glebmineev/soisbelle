package ru.spb.soisbelle.wrappers

import ru.spb.soisbelle.ProductEntity

/**
 * Created by gleb on 14.05.14.
 */
class CategoriesWithProducts {

  String categoryName
  boolean isEmpty = false
  List<ProductEntity> products = new ArrayList<ProductEntity>()

  CategoriesWithProducts(String categoryName, List<ProductEntity> products) {
    this.categoryName = categoryName
    this.products = products
    if (products.size() == 0)
      this.isEmpty = true
  }

}
