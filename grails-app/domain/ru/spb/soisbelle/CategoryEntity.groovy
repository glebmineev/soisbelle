package ru.spb.soisbelle

class CategoryEntity {

  String name
  String translitName
  String description

  long order

  CategoryEntity parentCategory

  static mappedBy = [
      endProducts: "endCategory",
      products: "categories"
  ]

  static mapping = {

    datasource 'ALL'

    table: 'category'
    columns {
      id column: 'category_id'
      name column: 'category_name'
      translitName column: 'category_translitname'
      description column: 'category_description'
      parentCategory column: 'category_parentcategory_id'
      order column: 'category_order'
      //selectedProducts joinTable: [name: 'category_product', key: 'category_id']
      //filters joinTable: [name: 'category_filter', key: 'category_id']
    }

    version false

    listCategory sort: "order", order: "asc", cascade: 'all-delete-orphan'
    filters cascade: 'all-delete-orphan'
  }

  static belongsTo = [FilterEntity]

  static hasMany = [
      products: ProductEntity,
      endProducts: ProductEntity,
      listCategory: CategoryEntity,
      filters: FilterEntity
  ]

  static constraints = {
    name nullable: true
    translitName nullable: true
    description nullable: true
  }

  public String toString() {
    return name
  }

}
