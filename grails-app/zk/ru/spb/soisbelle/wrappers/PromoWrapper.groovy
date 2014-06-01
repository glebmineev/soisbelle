package ru.spb.soisbelle.wrappers

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageService
import ru.spb.soisbelle.PromoEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES

/**
 * Created by gleb on 31.05.14.
 */
class PromoWrapper extends IdentWrapper implements Wrapper {

  String name;
  AImage image
  PromoWrapper memento;
  boolean editingStatus = false

  ImageService imageService = ApplicationHolder.getApplication().getMainContext().getBean("imageService");
  ServerFoldersService serverFoldersService = ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  public PromoWrapper() {}

  public PromoWrapper(PromoEntity promoEntity) {

    this.id = promoEntity.id
    this.name = promoEntity.name

    String path = new PathBuilder()
        .appendPath(serverFoldersService.promoPics)
        .appendString("${promoEntity.id}_${promoEntity.getName()}")
        .build()
    String std_name = STD_FILE_NAMES.PROMO_NAME.getName()
    int std_size = STD_IMAGE_SIZES.SMALL.getSize()

    this.image = imageService.getImageFile(path, std_name, std_size)

  }

  public PromoWrapper(PromoEntity promoEntity, STD_IMAGE_SIZES imageSize) {

    this.id = promoEntity.id
    this.name = promoEntity.name

    String path = new PathBuilder()
        .appendPath(serverFoldersService.promoPics)
        .appendString("${promoEntity.id}_${promoEntity.getName()}")
        .build()
    String std_name = STD_FILE_NAMES.PROMO_NAME.getName()
    int std_size = imageSize.getSize()

    this.image = imageService.getImageFile(path, std_name, std_size)

  }

  @Override
  @SuppressWarnings(value = "unchecked")
  protected PromoWrapper clone(){
    PromoWrapper cloned = new PromoWrapper()
    cloned.setId(id)
    cloned.setName(name)
    return cloned
  }

  @Override
  void restore() {
    this.name = memento.name
  }

}
