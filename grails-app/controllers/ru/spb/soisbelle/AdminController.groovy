package ru.spb.soisbelle

import ru.spb.soisbelle.login.URLUtils

class AdminController {

  LoginService loginService

  def index() { }

  def editor() { }

  def orders() { }

  def orderItem() { }

  def importCatalog() { }

  def products() { }

  def productItem() { }

  def manufacturers() {}

  def filters() { }

  def users() { }

  def info() { }

  def categories() { }

  def promo(){ }

  def saveInfo(){ }

  def beforeInterceptor = {
    //loginService.login("admin@admin.ru", "admin")
    List<String> groups = loginService.getUserGroups()
    if (!groups.contains("MANAGER")) {
      loginService.setParams(params)
      redirect(url: URLUtils.getHostUrl(request) + createLink(controller: "auth", action: "login"))
      return
    }
  }
}
