package ru.spb.soisbelle.zulModels.shop.sliders

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.wrappers.BulletWrapper

import java.util.concurrent.LinkedBlockingDeque

/**
 * Created by gleb on 17.05.14.
 */
class PromoSliderViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication
  ImageStorageService imageStorageService

  Deque<AImage> images = new LinkedBlockingDeque<AImage>(4);
  AImage currentBanner
  BulletWrapper[] pageWrappers = new BulletWrapper[4]
  BulletWrapper currentBullet

  @Init
  public void init(){
    imageStorageService = this.grailsApplication.getMainContext().getBean("imageStorageService")
    currentBanner = imageStorageService.getBanner_img1()
    images.add(imageStorageService.getBanner_img2())
    images.add(imageStorageService.getBanner_img3())
    images.add(imageStorageService.getBanner_img4())

    currentBullet = new BulletWrapper(0, "btn-sel")
    pageWrappers[0] = currentBullet
    pageWrappers[1] = new BulletWrapper(1, "btn")
    pageWrappers[2] = new BulletWrapper(2, "btn")
    pageWrappers[3] = new BulletWrapper(3, "btn")

  }

  @Command
  @NotifyChange(["currentBanner", "pageWrappers"])
  public void next(){
    images.addLast(currentBanner)
    currentBanner = images.removeFirst()
    moveForwardBullets(1)
  }

  @Command
  @NotifyChange(["currentBanner", "pageWrappers"])
  public void back(){
    AImage memory = currentBanner
    currentBanner = images.removeLast()
    images.addFirst(memory)
    moveBackBullets(1)
  }

  public void moveBackBullets(int steps) {
    int diff = currentBullet.getNumber() - steps
    currentBullet.setCstyle("btn")
    if (diff < 0) {
      currentBullet = pageWrappers[pageWrappers.length - 1]
    } else {
      currentBullet = pageWrappers[diff]
    }
    currentBullet.setCstyle("btn-sel")
  }

  public void moveForwardBullets(int steps) {
    int diff = currentBullet.getNumber() + steps
    currentBullet.setCstyle("btn")
    if (diff > (pageWrappers.length - 1)) {
      currentBullet = pageWrappers[0]
    } else {
      currentBullet = pageWrappers[diff]
    }
    currentBullet.setCstyle("btn-sel")
  }

  @Command
  @NotifyChange(["currentBanner", "pageWrappers"])
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
