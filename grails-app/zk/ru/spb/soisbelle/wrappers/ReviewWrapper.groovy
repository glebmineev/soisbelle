package ru.spb.soisbelle.wrappers

import ru.spb.soisbelle.ReviewsEntity

import java.text.SimpleDateFormat

/**
 * Created by gleb on 07.12.14.
 */
class ReviewWrapper extends IdentWrapper implements Wrapper {

  String fio
  String email
  String content
  String date
  Long rate

  public ReviewWrapper(ReviewsEntity entity) {
    this.fio = entity.getFio()
    this.email = entity.getEmail()
    this.content = entity.getContent()
    this.date = new SimpleDateFormat("dd-MM-yyyy").format(entity.getDate());
    this.rate = entity.getRate()

  }

  @Override
  void restore() {

  }

}
