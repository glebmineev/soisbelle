package ru.spb.soisbelle.zulModels.shop

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.wrappers.ManufacturerWrapper

class CarouselViewModel {

  ServerFoldersService serverFoldersService =
      ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  AImage nextImage
  AImage backImage

  List<ManufacturerWrapper> manufacturers = new ArrayList<ManufacturerWrapper>()
  List<ManufacturerEntity> entities;
  boolean endList = false
  int currentPos = 0
  int visibleElements = 6

  @Init
  public void init(){

    nextImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("better.png")
        .build())

    backImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("less.png")
        .build())

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
