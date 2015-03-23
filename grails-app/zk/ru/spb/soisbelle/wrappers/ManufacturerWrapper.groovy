package ru.spb.soisbelle.wrappers

import com.google.common.base.Strings
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageService
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES

class ManufacturerWrapper extends IdentWrapper implements Wrapper {

  String shortName
  String description

  ManufacturerWrapper memento
  boolean editingStatus = false

  AImage image

  ImageService imageService =
      ApplicationHolder.getApplication().getMainContext().getBean("imageService") as ImageService;

  ServerFoldersService serverFoldersService =
      ApplicationHolder.getApplication().getMainContext().
          getBean("serverFoldersService") as ServerFoldersService

  ManufacturerWrapper(ManufacturerEntity manufacturer) {
    this.id = manufacturer.id
    this.name = manufacturer.name
    this.shortName = manufacturer.shortName
    this.description = manufacturer.description

    String path = new PathBuilder()
        .appendPath(serverFoldersService.manufacturersPics)
        .appendString(Strings.isNullOrEmpty(manufacturer.picUuid) ? "" : manufacturer.picUuid)
        .build()
    String std_name = STD_FILE_NAMES.MANUFACTURER_NAME.getName()
    int std_size = STD_IMAGE_SIZES.SMALLEST.getSize()

    this.image = imageService.getImageFile(path, std_name, std_size)

  }

  public void restore(){
    this.id = memento.id
    this.name = memento.name
    this.shortName = memento.shortName
    this.description = memento.description
  }

}
