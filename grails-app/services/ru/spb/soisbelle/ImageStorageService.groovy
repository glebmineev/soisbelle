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
  AImage skipToStart
  AImage skipToEnd
  AImage rows
  AImage cells
  AImage pencil
  AImage failed


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

    skipToStart = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("skip_to_start.png")
        .build())

    skipToEnd = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("skip_to_end.png")
        .build())

    rows = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("rows.png")
        .build())

    cells = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("cells.png")
        .build())

    pencil = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("pencil.png")
        .build())

    failed = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendString("failed.png")
        .build())

  }

}
