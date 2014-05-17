package ru.spb.soisbelle.zulModels.admin

import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.event.Event
import ru.spb.soisbelle.InfoEntity

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 5/26/13
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
class InfoViewModel {

  InfoEntity infoEntity;

  String about;
  String contacts;
  String delivery;
  String details;

  @Init
  public void init(){
    infoEntity = InfoEntity.first()
    about = infoEntity.about
    contacts = infoEntity.contacts
    delivery = infoEntity.delivery
    details = infoEntity.details
  }

  @Command
  public void saveContacts(){
    InfoEntity.withTransaction {
      infoEntity.setContacts(contacts)
      infoEntity.save(flush: true)
    }
  }

  @Command
  public void saveAbout(){
    InfoEntity.withTransaction {
      infoEntity.setAbout(about)
      infoEntity.save(flush: true)
    }
  }

  @Command
  public void saveDelivery(){
    InfoEntity.withTransaction {
      infoEntity.setDelivery(delivery)
      infoEntity.save(flush: true)
    }
  }

  @Command
  public void saveDetails(){
    InfoEntity.withTransaction {
      infoEntity.setDetails(details)
      infoEntity.save(flush: true)
    }
  }

 String getFormattedString(String html){
   String formatted = html.replaceAll("(\\n\\t|\\n)", "");
   return formatted;
 }

}
