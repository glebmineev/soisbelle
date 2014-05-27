package ru.spb.soisbelle.zulModels.core

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.core.Domain
import ru.spb.soisbelle.wrappers.Wrapper

/**
 * Created by gleb on 5/5/14.
 */
abstract class SliderViewModel {

  ImageStorageService imageStorageService =
      ApplicationHolder.getApplication().getMainContext().getBean("imageStorageService") as ImageStorageService

  AImage nextImage
  AImage backImage

  List<Wrapper> glass = new ArrayList<Wrapper>()
  List<? extends Domain> entities

  //Сколько отображать на странице
  int showToPage
  //Индекс в списке всех товаров(allProducts)
  int currentPos
  //Показать или нет кнопку добавить.
  boolean endList = false
  //Показать или нет кнопку добавить.
  boolean startList = false

  //boolean endList = false
  //int currentPos = 0
  //int visibleElements = 6

  @Init
  public void init(){

    nextImage = imageStorageService.getNextImage()
    backImage = imageStorageService.getBackImage()

    setCountVisibleElement()
    prepare()

    if (entities.size() == 0)
      endList = true

    if (entities.size() == showToPage)
      endList = true

    if (entities.size() > 0 && !entities.isEmpty())
      moveCarousel()

  }

  public abstract void prepare()

  public abstract void setCountVisibleElement()

  public abstract Wrapper transformEntity(Domain entity)

  @Command
  @NotifyChange(["glass", "endList", "currentPos", "startList"])
  public void next(){
    currentPos++
    if (entities.size() <= (currentPos + showToPage)){
      endList = true
    }

    moveCarousel()
  }

  @Command
  @NotifyChange(["glass", "currentPos", "endList"])
  public void back(){
    currentPos--
    endList = false
    moveCarousel()
  }

  void moveCarousel(){
    glass.clear()
    entities.subList(currentPos, showToPage + currentPos).each {it ->
      glass.add(transformEntity(it))
    }
  }

}
