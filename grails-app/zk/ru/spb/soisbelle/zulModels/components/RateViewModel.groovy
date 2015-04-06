package ru.spb.soisbelle.zulModels.components

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ExecutionArgParam
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.ProductImageryWrapper
import ru.spb.soisbelle.wrappers.RateWrapper

/**
 * Created by gleb on 12.05.14.
 */
class RateViewModel {

  List<RateWrapper> rates = new ArrayList<RateWrapper>()

  RateWrapper currentRateWrapper = null

  ProductImageryWrapper product
  int countStars = 10

  boolean showUpdateRateButton = false

  ImageStorageService imageStorageService =
      ApplicationHolder.getApplication().getMainContext().getBean("imageStorageService") as ImageStorageService

  @Init
  public void init(@ExecutionArgParam("product") ProductImageryWrapper product) {
    this.product = product
    this.showUpdateRateButton = false
    fillRating(product)
  }

  /**
   * Заполнение панели рейтинга
   * @param product - товар
   */
  void fillRating(ProductImageryWrapper product) {
    rates.clear()

    int index = 0

    long rate = product.getRate()
    long people = product.getCountRatePeople() != null ? product.getCountRatePeople() : 0

    Float starsCoefficient = 0
    if (people != 0) {
      starsCoefficient = rate / people

      //Заполняем черные звезды
      int value = new BigDecimal(starsCoefficient).intValue()
      while (value > 0) {
        index = ++index
        rates.add(new RateWrapper(index, imageStorageService.getFullStar()))
        value--
      }

      //Заполняем получерные звезды
      if ((rate % people) > 0) {
        index = ++index
        rates.add(new RateWrapper(index, imageStorageService.getHalfStar()))
      }

    }
    //Заполняем белые звезды
    int countEmptyStars = (countStars - index)
    while (countEmptyStars > 0) {
      index = ++index
      rates.add(new RateWrapper(index, imageStorageService.getEmptyStar()))
      countEmptyStars--
    }
  }

  @Command
  @NotifyChange(["rates", "showUpdateRateButton"])
  public void addRateToProduct() {
    showUpdateRateButton = false
    ProductEntity.withTransaction {
      ProductEntity entity = ProductEntity.get(product.id)
      entity.setRate((product.getRate() + currentRateWrapper.getIndex()))
      entity.setCountRatePeople((product.getCountRatePeople() != null ? product.getCountRatePeople() : 0) + 1)
      if (entity.validate())
        entity.save(flush: true)

      product = new ProductImageryWrapper(entity)

      fillRating(product)

    }

  }

  @Command
  @NotifyChange(["rates", "showUpdateRateButton"])
  public void changeRate(@BindingParam("rate") RateWrapper rateWrapper){
    showUpdateRateButton = true
    rates.clear()
    int index = 0
    int selectedIndex = rateWrapper.getIndex()
    while (selectedIndex > 0) {
      index = ++index
      rates.add(new RateWrapper(index, imageStorageService.getFullStar()))
      selectedIndex--
    }

    int countEmptyStars = (countStars - index)
    while (countEmptyStars > 0) {
      index = ++index
      rates.add(new RateWrapper(index, imageStorageService.getEmptyStar()))
      countEmptyStars--
    }

    this.currentRateWrapper = rateWrapper

  }

}
