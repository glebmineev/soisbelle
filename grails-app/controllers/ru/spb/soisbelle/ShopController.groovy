package ru.spb.soisbelle

class ShopController {

  def loginService

  def index() {
    loginService.setParams(params)
  }

  def catalog() {
    Long categoryId = params.get("category") as Long
    CategoryEntity entity = CategoryEntity.get(categoryId)
    loginService.setParams(params)
    return [title: entity.getName()]
  }

  def product() {
    Long productId = params.get("product") as Long
    ProductEntity entity = ProductEntity.get(productId)
    loginService.setParams(params)
    return [title: entity.getName(), description: entity.description, keywords : "косметика.краски"]
  }

  def checkout() {
    loginService.setParams(params)
  }

  def success() {
    loginService.setParams(params)
  }

  def register() {
    loginService.setParams(params)
  }

  def cart() {
    loginService.setParams(params)
  }

  def about() {
    loginService.setParams(params)
  }

  def details() {
    loginService.setParams(params)
  }

  def contacts() {
    loginService.setParams(params)
  }

  def delivery() {
    loginService.setParams(params)
  }

  def aboutShop() {
    loginService.setParams(params)
  }

  def howToBuy() {
    loginService.setParams(params)
  }

  def seeYouEmail() {
    loginService.setParams(params)
  }

  def activate() {
    loginService.setParams(params)
  }

  def nonActivate () {
    loginService.setParams(params)
  }

  def manufacturers() {
    loginService.setParams(params)
  }

}
