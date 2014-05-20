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
  AImage fullStar
  AImage halfStar
  AImage emptyStar
  AImage closeWnd
  AImage progressRoll
  AImage banner_img1
  AImage banner_img2
  AImage banner_img3
  AImage banner_img4


  void afterPropertiesSet() {

    nextImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("better.png")
        .build())

    backImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("less.png")
        .build())

    fullStar = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("star_full.png")
        .build())

    halfStar = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("star_half.png")
        .build())

    emptyStar = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("star_empty.png")
        .build())

    closeWnd = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("failed.png")
        .build())

    progressRoll = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("ajax-loader.gif")
        .build())

    banner_img1 = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("slider")
        .appendString("FON2_b.jpg")
        .build())

    banner_img2 = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("slider")
        .appendString("FON3_b.jpg")
        .build())

    banner_img3 = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("slider")
        .appendString("FON2_b.jpg")
        .build())

    banner_img4 = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("slider")
        .appendString("FON3_b.jpg")
        .build())

  }

}
