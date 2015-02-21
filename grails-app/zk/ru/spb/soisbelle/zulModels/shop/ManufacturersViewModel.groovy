package ru.spb.soisbelle.zulModels.shop

import org.zkoss.bind.annotation.ExecutionArgParam
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.CategoriesWithProducts
import ru.spb.soisbelle.wrappers.ManufacturerWrapper
import ru.spb.soisbelle.wrappers.ProductWrapper

/**
 * Created by gleb on 14.05.14.
 */
//TODO:: по какому принципу отбирать товары категорий.
class ManufacturersViewModel {

  List<CategoriesWithProducts> categoriesWithProducts = new ArrayList<CategoriesWithProducts>()
  ManufacturerWrapper manufacturerWrapper

  @Init
  public void init() {
    Long manufacturer_id = Executions.getCurrent().getParameter("manufacturer") as Long
    ManufacturerEntity manufacturer = ManufacturerEntity.get(manufacturer_id)

    manufacturerWrapper = new ManufacturerWrapper(manufacturer);

    CategoryEntity.findAllWhere(parentCategory: null).each {it ->
      CategoryEntity mainCategory = CategoryEntity.get(it.getId())
      List<ProductEntity> products = collectAllProducts(mainCategory, new ArrayList<ProductEntity>())

      ArrayList<ProductEntity> find = products.findAll { product ->
        product.manufacturer.name.equals(manufacturer.name)
      }

      int size = find.size()
      List<ProductEntity> prs = find.subList(0, size >= 5 ? 5 : size)

      categoriesWithProducts.add(new CategoriesWithProducts(mainCategory.getName(), prs))

    }
    int  u = 0
  }

  /**
   * Собираем все товары корневой категории
   * @param category - категория
   * @param products - извеченные товары
   * @return - список товаров данной категории
   */
  List<ProductEntity> collectAllProducts(CategoryEntity category, List<ProductEntity> products) {
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
