package ru.spb.soisbelle.zulModels.shop.sliders

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.wrappers.BulletWrapper
import ru.spb.soisbelle.wrappers.PromoWrapper

import java.util.concurrent.LinkedBlockingDeque

/**
 * Created by gleb on 17.05.14.
 */
class PromoSliderViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication
  InitService initService

  Deque<PromoWrapper> images = new LinkedBlockingDeque<PromoWrapper>(4);
  PromoWrapper currentPromo
  BulletWrapper[] pageWrappers = new BulletWrapper[4]
  BulletWrapper currentBullet

  @Init
  public void init(){
    initService = grailsApplication.getMainContext().getBean("initService")


    ArrayList<PromoWrapper> promos = initService.promos
    if (promos.size() != 0) {
      currentPromo = initService.promos.get(0)
      images.addAll(initService.promos.subList(1, initService.promos.size()))

      currentBullet = new BulletWrapper(0, "active")
      pageWrappers[0] = currentBullet
      pageWrappers[1] = new BulletWrapper(1, "nonactive")
      pageWrappers[2] = new BulletWrapper(2, "nonactive")
      pageWrappers[3] = new BulletWrapper(3, "nonactive")
    }
  }

  @Command
  @NotifyChange(["currentPromo", "pageWrappers"])
  public void next(){
    images.addLast(currentPromo)
    currentPromo = images.removeFirst()
    moveForwardBullets(1)
  }

  @Command
  @NotifyChange(["currentPromo", "pageWrappers"])
  public void back(){
    PromoWrapper memory = currentPromo
    currentPromo = images.removeLast()
    images.addFirst(memory)
    moveBackBullets(1)
  }

  public void moveBackBullets(int steps) {
    int diff = currentBullet.getNumber() - steps
    currentBullet.setCstyle("nonactive")
    if (diff < 0) {
      currentBullet = pageWrappers[pageWrappers.length - 1]
    } else {
      currentBullet = pageWrappers[diff]
    }
    currentBullet.setCstyle("active")
  }

  public void moveForwardBullets(int steps) {
    int diff = currentBullet.getNumber() + steps
    currentBullet.setCstyle("nonactive")
    if (diff > (pageWrappers.length - 1)) {
      currentBullet = pageWrappers[0]
    } else {
      currentBullet = pageWrappers[diff]
    }
    currentBullet.setCstyle("active")
  }

  @Command
  @NotifyChange(["currentPromo", "pageWrappers"])
  public void selectSlide(@BindingParam("bullet") BulletWrapper bulletWrapper){
    int diff = bulletWrapper.getNumber() - currentBullet.getNumber()
    if (diff < 0) {
      int diffMod = Math.abs(diff)
      while(diffMod > 0){
        back()
        diffMod--
      }
    } else {
      while(diff > 0){
        next()
        diff--
      }
    }

  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
