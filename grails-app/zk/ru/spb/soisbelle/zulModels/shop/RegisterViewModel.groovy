package ru.spb.soisbelle.zulModels.shop

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.EmailService
import ru.spb.soisbelle.RoleEntity
import ru.spb.soisbelle.UserEntity
import ru.spb.soisbelle.captcha.RandomStringGenerator

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RegisterViewModel {

  EmailService emailService = ApplicationHolder.getApplication().getMainContext().getBean("emailService") as EmailService

  //Логгер
  static Logger log = LoggerFactory.getLogger(RegisterViewModel.class)

  String login
  String password
  String repassword
  String fio
  String phone
  String email
  String address

  RandomStringGenerator rsg = new RandomStringGenerator(6);
  String captcha = rsg.getRandomString()
  String captchaInput

  ExecutorService service = Executors.newScheduledThreadPool(2);

  @Init
  public void init() { }

  @Command
  public void register(){
    try {
      createNewUser()
    } catch (Exception ex) {
      log.debug(ex.getMessage())
    }
  }

  void createNewUser(){
    UserEntity.withTransaction {
      UserEntity user = new UserEntity()
      user.setLogin(login)
      user.setPassword(password.encodeAsSHA1())
      user.setFio(fio)
      user.setPhone(phone)
      user.setEmail(email)
      user.setAddress(address)

      String hash = UUID.randomUUID().toString().replaceAll("-", "")
      user.setActivateCode(hash)

      RoleEntity group = RoleEntity.findByName("USER")
      user.addToGroups(group)

      if (user.validate()) {
        user.save(flush: true)
        service.execute(new Runnable() {

          @Override
          void run() {
            emailService.sendConfirmationEmail(user.getLogin(), user.getEmail(), hash)
          }

        })

        Executions.sendRedirect("/shop/seeYouEmail")
      }
    }
  }

  @Command
  @NotifyChange(["captcha"])
  public void regenerate(){
    captcha = rsg.getRandomString()
  }

}
