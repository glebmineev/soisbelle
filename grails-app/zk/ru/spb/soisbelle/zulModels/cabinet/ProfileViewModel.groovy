package ru.spb.soisbelle.zulModels.cabinet

import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.ImageService
import ru.spb.soisbelle.LoginService
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.UserEntity
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.HrefWrapper
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

@Init(superclass=true)
class ProfileViewModel extends DownloadImageViewModel {

  String fio
  String phone
  String email
  String address
  AImage img

  LoginService loginService = ApplicationHolder.getApplication().getMainContext().getBean("loginService") as LoginService

  //Навигация.
  List<HrefWrapper> links = new LinkedList<HrefWrapper>()

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.PRODUCT_NAME.getName()
    std_image_size = STD_IMAGE_SIZES.MIDDLE.getSize()
    targetImage = "targetImage"
  }

  @Override
  void initialize() {
    UserEntity user = loginService.getCurrentUser()
    fio = user.fio
    phone = user.phone
    email = user.email
    address = user.address

    String path = new PathBuilder()
        .appendPath(serverFoldersService.userPics)
        .appendString(loginService.getCurrentUser().email)
        .build()
    String std_name = STD_FILE_NAMES.CATEGORY_NAME.getName()
    int std_size = STD_IMAGE_SIZES.MIDDLE.getSize()

    img = imageService.getImageFile(path, std_name, std_size)

    initLinks()

  }

  void initLinks() {
    links.add(new HrefWrapper("Личный кабинет", "/cabinet"))
    links.add(new HrefWrapper("Редактирование профиля", "/cabinet/profile"))
  }

  @Command
  public void save() {
    UserEntity user = loginService.getCurrentUser()
    if (uuid != null) {
      File temp = new File(new PathBuilder()
          .appendPath(serverFoldersService.temp)
          .appendString(uuid)
          .build())

      File store = new File(new PathBuilder()
          .appendPath(serverFoldersService.userPics)
          .appendString(user.email)
          .build())
      if (!store.exists())
        store.mkdirs()
      FileUtils.copyDirectory(temp, store)
    }

    user.fio = fio
    user.phone = phone
    user.email = email
    user.address = address

    Executions.sendRedirect("/cabinet/index")

  }

}
