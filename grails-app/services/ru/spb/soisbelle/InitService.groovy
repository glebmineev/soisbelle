package ru.spb.soisbelle

import com.google.common.collect.Lists
import org.springframework.beans.factory.InitializingBean
import ru.spb.soisbelle.common.CategoryHandler
import ru.spb.soisbelle.wrappers.CategoryWrapper

class InitService implements InitializingBean {

  static transactional = true

  List<ProductEntity> recommended
/*  List<ProductEntity> popular
  List<ProductEntity> hits*/
  List<CategoryEntity> categoriesThru = new ArrayList<CategoryEntity>()
  List<CategoryWrapper> categories = new ArrayList<CategoryWrapper>()
  List<ManufacturerEntity> manufacturers

  String about
  String contacts
  String delivery
  String details
  String aboutMagazine
  String howToBuy

  Map<String, List<ProductEntity>> hits = new HashMap<String, List<ProductEntity>>()

  void afterPropertiesSet() {
    categoriesThru.clear()
    categories.clear()
    categoriesThru.addAll(CategoryEntity.findAllWhere(parentCategory: null))
    categoriesThru.each {it ->
      CategoryWrapper wrapper = new CategoryWrapper(it)
      categories.add(wrapper)
    }

    initInfo()

    manufacturers = ManufacturerEntity.list()
    //TODO:: сделать в админке.
    recommended = ProductEntity.list(max: 6)
    //TODO:: Хиты апдейтить каждый день.
    updateHits()
    //popular = ProductEntity.list(max: 6)
    //hits = ProductEntity.list(max: 5)
  }

  void initInfo() {
    about = InfoEntity.findByName("О компании")?.getDescription()
    contacts = InfoEntity.findByName("Контакты")?.getDescription()
    delivery = InfoEntity.findByName("Доставка")?.getDescription()
    details = InfoEntity.findByName("Информация")?.getDescription()
    aboutMagazine = InfoEntity.findByName("О магазине")?.getDescription()
    howToBuy = InfoEntity.findByName("Как покупать")?.getDescription()
  }

  void updateHits(){
    categoriesThru.each {it ->
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
