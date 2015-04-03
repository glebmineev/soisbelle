package ru.spb.soisbelle.zulModels.cabinet

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.LoginService
import ru.spb.soisbelle.UserEntity
import ru.spb.soisbelle.wrappers.HrefWrapper

class ChangePassViewModel {

  /**
   * Старый пароль.
   */
  String oldPass

  /**
   * новый пароль.
   */
  String newPass

  /**
   * Повтрение нового пароля.
   */
  String retypeNewPass

  LoginService loginService =
    ApplicationHolder.getApplication().getMainContext().getBean("loginService") as LoginService

  //Навигация.
  List<HrefWrapper> links = new LinkedList<HrefWrapper>()

  @Init
  public void init(){
    links.add(new HrefWrapper("Личный кабинет", "/cabinet"))
    links.add(new HrefWrapper("Смена пароля", "/cabinet/changePass"))
  }

  @Command
  public void changePass(){
    UserEntity user = loginService.getCurrentUser()
    UserEntity.withTransaction {status ->
      user.setPassword(newPass.encodeAsSHA1())
      user.save(flush: true)
    }
    Executions.sendRedirect("/cabinet/successOperation")
  }

}
