package ru.spb.soisbelle

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.PathUtils

/**
 * Бин отвечает за центролизованный доступ к путям до директорий приложения.
 */
class ServerFoldersService implements InitializingBean {

  static transactional = true

  //Логгер
  static Logger log = LoggerFactory.getLogger(ServerFoldersService.class)

  String fileSeparator = System.getProperty("file.separator")

  /**
   * Корневой каталог картинок.
   */
  private final static String pictures = "pictures";

  /**
   * Путь до каталога выгрузки excel заказов.
   */
  String orders;
  /**
   * Путь до каталога с картинками товаров.
   */
  String productImages
  /**
   * Путь до каталога с картинками сайта.
   */
  String images
  /**
   * Путь до каталога с картинками производителей.
   */
  String manufacturersPics
  /**
   * Путь до каталога с картинками категорий.
   */
  String categoriesPics
  /**
   * Путь до каталога с аваторкми пользователей.
   */
  String userPics
  /**
   * Путь до каталога с промо баннерами.
   */
  String promoPics
  /**
   * Путь до каталога с временными файлами.
   */
  String temp

  @Override
  void afterPropertiesSet() throws Exception {
    PathUtils stringUtils = new PathUtils()
    String root = ApplicationHolder.application.mainContext.servletContext.getRealPath(fileSeparator)
    log.info("PATH_TO_SERVER_FOLDER " + root)
    String twoLevelUp = stringUtils.buildPath(2, root)
    log.info("PATH_TO_SERVER_FOLDER_TWO_LEVEL_UP " + twoLevelUp)

    orders = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendPath("admin")
        .appendString("orders")
        .checkDir()
        .build()

    images = new PathBuilder()
        .appendPath(root)
        .appendString("images")
        .checkDir()
        .build()


    userPics = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendString("userPics")
        .checkDir()
        .build()

    productImages = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendString("productImages")
        .checkDir()
        .build()

    manufacturersPics  = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendString("manufacturersPics")
        .checkDir()
        .build()

    categoriesPics  = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendString("categoriesPics")
        .checkDir()
        .build()

    promoPics  = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendString("promoPics")
        .checkDir()
        .build()

    temp = new PathBuilder()
        .appendPath(twoLevelUp)
        .appendPath(pictures)
        .appendString("temp")
        .checkDir()
        .build()

  }

}
