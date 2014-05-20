package ru.spb.soisbelle.zulModels.shop.sliders

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageStorageService

import java.util.concurrent.LinkedBlockingDeque

/**
 * Created by gleb on 17.05.14.
 */
class PromoSliderViewModel implements GrailsApplicationAware{

  GrailsApplication grailsApplication
  ImageStorageService imageStorageService

  Deque<AImage> images = new LinkedBlockingDeque<AImage>(4);
  AImage currentBanner

  @Init
  public void init(){
    imageStorageService = this.grailsApplication.getMainContext().getBean("imageStorageService")
    currentBanner = imageStorageService.getBanner_img1()
    images.add(imageStorageService.getBanner_img2())
    images.add(imageStorageService.getBanner_img3())
    images.add(imageStorageService.getBanner_img4())
    images.add(currentBanner)
  }

  @Command
  @NotifyChange(["currentBanner"])
  public void next(){
    currentBanner = images.removeFirst()
    images.add(currentBanner)
  }

  @Command
  @NotifyChange(["currentBanner"])
  public void back(){
    currentBanner = images.removeLast()
    images.add(currentBanner)
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
