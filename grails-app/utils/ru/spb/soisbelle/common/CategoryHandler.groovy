package ru.spb.soisbelle.common

import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.CategoryWrapper

class CategoryHandler {

  /**
   * Собирает все продукты из категорий.
   * @param category - категория с товарами.
   * @param products - итоговый список товаров.
   * @return - все продукты категорий.
   */
  static List<ProductEntity> collectAllProducts(CategoryEntity category, List<ProductEntity> products) {
    List<CategoryEntity> categories = category.listCategory as List<CategoryEntity>
    if (categories != null && categories.size() > 0)
      categories.each { CategoryEntity it ->
        if (it.listCategory != null && it.listCategory.size() > 0)
          collectAllProducts(it, products)
        else
          products.addAll(it.products as List<ProductEntity>)
      }
    else {
      products.addAll(category.products as List<ProductEntity>)
    }

    return products
  }

}
