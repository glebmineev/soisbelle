package ru.spb.soisbelle.zulModels.auth

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.EmailService
import ru.spb.soisbelle.UserEntity
import ru.spb.soisbelle.common.PasswordGenerator

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class DropPassViewModel {

  //Почта пользователя.
  String email
  //Генератор пароля
  PasswordGenerator pg = new PasswordGenerator(9)
  //Сервис отсыла письма с паролем
  EmailService emailService =
    ApplicationHolder.getApplication().getMainContext().getBean("emailService") as EmailService

  ExecutorService service = Executors.newScheduledThreadPool(2);

  @Init
  public void init(){ }

  @Command
  public void dropPass(){

    UserEntity user = UserEntity.findByEmail(email)
    String new_pass = pg.getRandomString()
    if (user != null) {
      UserEntity.withTransaction {status ->
        user.setPassword(new_pass.encodeAsSHA1())
        user.save(flush: true)
      }

      service.execute(new Runnable() {

        @Override
        void run() {
          emailService.sendPassEmail(email, new_pass)
        }

      })

    }

    Executions.sendRedirect("/shop/success")

  }

}
