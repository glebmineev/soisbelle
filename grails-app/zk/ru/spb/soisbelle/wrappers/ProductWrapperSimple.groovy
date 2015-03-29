package ru.spb.soisbelle.wrappers

import com.google.common.base.Strings
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.image.AImage
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.*
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 4/27/13
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
class ProductWrapperSimple extends IdentWrapper {

  String article
  Long price
  String usage
  String description
  String shortDescription
  String volume
  Long countToStock = 0
  Long rate = 0
  Long countRatePeople

  ManufacturerEntity manufacturer
  FilterEntity filter

  boolean editingStatus = false

  Long totalPrice;
  Long count;

  public ProductWrapperSimple(ProductEntity productEntity) {

    fillStandardFields(productEntity)

  }

  private void fillStandardFields(ProductEntity productEntity) {
    this.id = productEntity.id
    this.name = productEntity.name
    this.article = productEntity.article
    this.price = productEntity.price
    this.volume = productEntity.volume
    this.usage = productEntity.usage
    this.description = productEntity.description
    this.countToStock = productEntity.countToStock
    this.manufacturer = productEntity.manufacturer
    this.filter = productEntity.filter

    String descShort = productEntity.description
    this.shortDescription = Strings.isNullOrEmpty(descShort) ? "" : "${descShort.substring(0, 30)} ... "


    this.countRatePeople = productEntity.countRatePeople != null ? productEntity.getCountRatePeople() : 0
    this.rate = productEntity.rate != null ? productEntity.getRate() : 0
  }

}
