package ru.spb.soisbelle.zulModels.admin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import ru.spb.soisbelle.EmailConfigEntity

class EmailConfigViewModel {

  //Логгер
  static Logger log = LoggerFactory.getLogger(EmailConfigViewModel.class)

  String host
  String port
  String auth
  String starttls
  String login
  String password

  @Init
  public void init() {
    EmailConfigEntity emailConfig = EmailConfigEntity.findByName("Конфигурация")
    host = emailConfig.getHost()
    port = emailConfig.getPort()
    auth = emailConfig.getAuth()
    starttls = emailConfig.getStarttls()
    login = emailConfig.getLogin()
    password = emailConfig.getPassword()
  }

  @Command
  public void saveConfig(){
    saveEmailConfig()
  }

  void saveEmailConfig(){
    EmailConfigEntity.withTransaction {
      EmailConfigEntity emailConfig = EmailConfigEntity.findByName("Конфигурация");
      emailConfig.setHost(host)
      emailConfig.setPort(port)
      emailConfig.setAuth(auth)
      emailConfig.setStarttls(starttls)
      emailConfig.setLogin(login)
      emailConfig.setPassword(password)

      if (emailConfig.validate()) {
        emailConfig.save(flush: true)

        //Executions.sendRedirect("/shop/seeYouEmail")
      }
    }
  }

}
