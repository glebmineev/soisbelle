package ru.spb.soisbelle

import com.google.common.collect.Lists
import org.springframework.beans.factory.InitializingBean
import ru.spb.soisbelle.common.CategoryHandler

class InitService implements InitializingBean {

  static transactional = true

  List<ProductEntity> recommended
/*  List<ProductEntity> popular
  List<ProductEntity> hits*/
  List<CategoryEntity> categories
  List<ManufacturerEntity> manufacturers
  InfoEntity info

  Map<String, List<ProductEntity>> hits = new HashMap<String, List<ProductEntity>>()

  void afterPropertiesSet() {
    categories = CategoryEntity.findAllWhere(parentCategory: null)
    info = InfoEntity.first()
    manufacturers = ManufacturerEntity.list()
    //TODO:: сделать в админке.
    recommended = ProductEntity.list(max: 4)
    //TODO:: Хиты апдейтить каждый день.
    updateHits()
    //popular = ProductEntity.list(max: 6)
    //hits = ProductEntity.list(max: 5)
  }

  void updateHits(){
    categories.each {it ->
      List<ProductEntity> products = CategoryHandler.collectAllProducts(it, Lists.newArrayList())
      Collections.sort(products)
      if (products != null) {
        int max = 4
        if (products.size() < 4)
          max = products.size()
        hits.put(it.name, products.subList(0, max))
      }

    }
  }

  List<ProductEntity> getHits(String categoryName) {
    hits.get(categoryName)
  }

  void refreshShop() {
    afterPropertiesSet()
  }

}
