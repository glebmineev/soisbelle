package ru.spb.soisbelle.wrappers

import org.zkoss.image.AImage

/**
 * Created by gleb on 12.05.14.
 */
class RateWrapper {

  int index
  AImage star

  RateWrapper(int index, AImage star) {
    this.index = index
    this.star = star
  }

}
