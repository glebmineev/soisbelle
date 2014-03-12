package ru.spb.soisbelle

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.zkoss.image.AImage
import ru.spb.soisbelle.common.PathBuilder

class ImageStorageService implements InitializingBean {

  static transactional = true
  //Логгер
  static Logger log = LoggerFactory.getLogger(ImageService.class)

  ServerFoldersService serverFoldersService =
      ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  AImage nextImage
  AImage backImage

  void afterPropertiesSet() {

    nextImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("better.png")
        .build())

    backImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("less.png")
        .build())

  }

}
