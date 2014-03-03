package ru.spb.soisbelle

import ru.spb.soisbelle.login.URLUtils

class AuthController {

  def initService
  def loginService

  def index(){ }

  def login(){
    return [mainCategoties: initService.categories]
  }

  def logout() {
    loginService.logout()
    redirect(url: URLUtils.getHostUrl(request) + createLink(
        controller: loginService.params.controller,
        action: loginService.params.action,
        params: loginService.params))
  }

  def dropPass() {
    return [mainCategoties: initService.categories]
  }

}
