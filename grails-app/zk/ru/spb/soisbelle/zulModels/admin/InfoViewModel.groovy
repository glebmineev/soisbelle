package ru.spb.soisbelle.zulModels.admin

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import ru.spb.soisbelle.InfoEntity
import ru.spb.soisbelle.InitService

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 5/26/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class InfoViewModel  implements GrailsApplicationAware {

  GrailsApplication grailsApplication
  InitService initService

  String about
  String contacts
  String delivery
  String details
  String aboutMagazine
  String howToBuy

  @Init
  public void init() {

    initService = grailsApplication.mainContext.getBean("initService")

    about = initService.about
    contacts = initService.contacts
    delivery = initService.delivery
    details = initService.details
    aboutMagazine = initService.aboutMagazine
    howToBuy = initService.howToBuy

  }

  @Command
  public void saveContacts() {
    InfoEntity.withTransaction {
      InfoEntity contactsEntity = InfoEntity.findByName("Контакты")
      contactsEntity.setDescription(contacts)
      contactsEntity.save(flush: true)
    }
    initService.initInfo()
  }

  @Command
  public void saveAbout() {
    InfoEntity.withTransaction {
      InfoEntity aboutEntity = InfoEntity.findByName("О компании")
      aboutEntity.setDescription(about)
      aboutEntity.save(flush: true)
    }
    initService.initInfo()
  }

  @Command
  public void saveDelivery() {
    InfoEntity.withTransaction {
      InfoEntity deliveryEntity = InfoEntity.findByName("Доставка")
      deliveryEntity.setDescription(delivery)
      deliveryEntity.save(flush: true)
    }
    initService.initInfo()
  }

  @Command
  public void saveDetails() {
    InfoEntity.withTransaction {
      InfoEntity detailsEntity = InfoEntity.findByName("Информация")
      detailsEntity.setDescription(details)
      detailsEntity.save(flush: true)
    }
    initService.initInfo()
  }

  @Command
  public void saveHowToBuy(){
    InfoEntity.withTransaction {
      InfoEntity howToBuyEntity = InfoEntity.findByName("Как покупать")
      howToBuyEntity.setDescription(howToBuy)
      howToBuyEntity.save(flush: true)
    }
    initService.initInfo()
  }

  @Command
  public void saveAboutMagazine(){
    InfoEntity.withTransaction {
      InfoEntity aboutMagazineEntity = InfoEntity.findByName("О магазине")
      aboutMagazineEntity.setDescription(aboutMagazine)
      aboutMagazineEntity.save(flush: true)
    }
    initService.initInfo()
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
