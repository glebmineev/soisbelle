package ru.spb.soisbelle.zulModels.admin

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import ru.spb.soisbelle.EmailService
import ru.spb.soisbelle.EmailTemplateEntity

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 5/26/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class EmailsViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication
  EmailService emailService

  String passwordRecovery
  String newOrder
  String registration

  @Init
  public void init() {

    emailService = grailsApplication.mainContext.getBean("emailService")

    passwordRecovery = emailService.passwordRecoveryTemplate
    newOrder = emailService.newOrderTemplate
    registration = emailService.registrationTemplate

  }

  @Command
  public void savePasswordRecovery() {
    EmailTemplateEntity.withTransaction {
      EmailTemplateEntity passwordRecoveryEntity = EmailTemplateEntity.findByName("Восстановление пароля")
      passwordRecoveryEntity.setEmailHtml(passwordRecovery)
      passwordRecoveryEntity.save(flush: true)
    }
    emailService.initTemplates()
  }

  @Command
  public void saveNewOrder() {
    EmailTemplateEntity.withTransaction {
      EmailTemplateEntity newOrderEntity = EmailTemplateEntity.findByName("Заказ")
      newOrderEntity.setEmailHtml(newOrder)
      newOrderEntity.save(flush: true)
    }
    emailService.initTemplates()
  }

  @Command
  public void saveRegistration() {
    EmailTemplateEntity.withTransaction {
      EmailTemplateEntity registrationEntity = EmailTemplateEntity.findByName("Регистрация")
      registrationEntity.setEmailHtml(registration)
      registrationEntity.save(flush: true)
    }
    emailService.initTemplates()
  }

  String getFormattedString(String html) {
    String formatted = html.replaceAll("(\\n\\t|\\n)", "");
    return formatted;
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
