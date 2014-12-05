package ru.spb.soisbelle

import ru.spb.soisbelle.annotation.FieldInfo
import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.zulModels.admin.filters.data.FilterTypes

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
class ProductEntity implements Comparable, Domain {

  //static searchable = [except: '']
  String engImagePath
  String imagePath

  @FieldInfo(isFilter = true, type = FilterTypes.TEXT_FIELD)
  String article
  @FieldInfo(isFilter = true, type = FilterTypes.TEXT_FIELD)
  String name
  String description
  String usage
  String volume
  @FieldInfo(isFilter = true, type = FilterTypes.MEASURE_FIELD)
  Long price
  @FieldInfo(isFilter = true, type = FilterTypes.NUMBER_FIELD)
  Long countToStock

  FilterEntity filter

  ProductPropertyEntity productProperty
  @FieldInfo(isFilter = true, type = FilterTypes.COMBO_FIELD)
  ManufacturerEntity manufacturer

  CategoryEntity endCategory

  //Оценки товара
  Long rate

  //Количество проголосовавших людей
  Long countRatePeople

  static belongsTo = [CategoryEntity]

  static mapping = {

    table: 'product'
    columns {
      id column: 'product_id'
      imagePath column: 'product_imagepath', type: 'text'
      engImagePath column: 'product_engimagepath', type: 'text'
      article column: 'product_article'
      name column: 'product_name', index:'product_name_index'
      description column: 'product_description', index:'product_description_index'
      usage column: 'product_usage'
      volume column: 'product_volume'
      productProperty column: 'product_productproperty_id'
      price column: 'product_price'
      manufacturer fetch: "join", column: 'product_manufacturer_id', index:'product_manufacturer_id_index'
      countToStock column: 'product_counttostock'
      filter fetch: "join", column: 'product_filter'
      rate column: 'product_rate'
      countRatePeople column: 'count_rate_people'
      endCategory column: 'category_end_category_id'
    }

    version false

    listOrderProduct cascade: 'all-delete-orphan'
    reviews cascade: 'all-delete-orphan'
  }

  static hasMany = [
      listOrderProduct: OrderProductEntity,
      reviews: ReviewsEntity,
      categories: CategoryEntity
  ]

  static constraints = {
    imagePath maxSize: 65535, nullable: true
    engImagePath maxSize: 65535, nullable: true
    article nullable: true
    name maxSize: 65535, nullable: true
    description maxSize: 65535, nullable: true
    usage maxSize: 65535, nullable: true
    volume nullable: true
    price nullable: true
    productProperty nullable: true
    manufacturer nullable: false
    countToStock nullable: true
    filter nullable: true
    rate nullable: true
    countRatePeople nullable: true
    endCategory nullable: true
  }

  public String toString() {
    return name
  }

  @Override
  int compareTo(Object o) {
    ProductEntity item = (ProductEntity) o
    if (item.name > this.name)
      return 1
    if (item.name < this.name)
      return -1
    if (item.name.equals(this.name))
      return 0

  }

}
