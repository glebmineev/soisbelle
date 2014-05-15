package ru.spb.soisbelle.zulModels.shop

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.Window
import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.wrappers.ManufacturerWrapper
import ru.spb.soisbelle.wrappers.Wrapper

class CarouselViewModel {

  ImageStorageService imageStorageService =
      ApplicationHolder.getApplication().getMainContext().getBean("imageStorageService") as ImageStorageService

  AImage nextImage
  AImage backImage

  List<? extends Wrapper> manufacturers = new ArrayList<? extends Wrapper>()
  List<? extends Domain> entities;
  boolean endList = false
  int currentPos = 0
  int visibleElements = 6

  @Init
  public void init(){

    nextImage = imageStorageService.getNextImage()
    backImage = imageStorageService.getBackImage()

    entities = ManufacturerEntity.list(sort: "name")
    if (entities.size() <= visibleElements)
      endList = true

    moveCarousel()
  }

  @Command
  @NotifyChange(["manufacturers", "endList", "currentPos"])
  public void next(){
    currentPos++
    if (entities.size() <= (currentPos + visibleElements)){
      endList = true
    }
    moveCarousel()
  }

  @Command
  @NotifyChange(["manufacturers", "currentPos", "endList"])
  public void back(){
    currentPos--
    endList = false
    moveCarousel()
  }

  void moveCarousel(){
    manufacturers.clear()
    entities.subList(currentPos, visibleElements + currentPos).each {it ->
      manufacturers.add(new  ManufacturerWrapper(it))
    }
  }

}
