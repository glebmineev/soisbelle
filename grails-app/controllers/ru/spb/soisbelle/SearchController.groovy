package ru.spb.soisbelle

class SearchController {

  def initService

  def index() {
    if (!params.query?.trim()) {
      return [mainCategoties: initService.categories]
    }

  }

}
