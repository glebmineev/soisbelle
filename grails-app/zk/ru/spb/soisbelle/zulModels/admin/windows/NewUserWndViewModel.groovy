package ru.spb.soisbelle.zulModels.admin.windows

import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zkplus.databind.BindingListModelList
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Window
import ru.spb.soisbelle.LoginService
import ru.spb.soisbelle.RoleEntity
import ru.spb.soisbelle.UserEntity
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.RoleWrapper
import ru.spb.soisbelle.wrappers.UserWrapper
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

@Init(superclass=true)
class NewUserWndViewModel extends DownloadImageViewModel {

  //Логгер
  static Logger log = LoggerFactory.getLogger(ManufacturerWndViewModel.class)

  Long id
  String login
  String password
  String repassword

  String fio
  String phone
  String email
  String address

  AImage image

  boolean isActive = false
  ListModelList<RoleWrapper> roles

  LoginService loginService =
    ApplicationHolder.getApplication().getMainContext().getBean("loginService") as LoginService

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.USER_NAME.getName()
    std_image_size = STD_IMAGE_SIZES.MIDDLE.getSize()
    targetImage = "targetImage"
  }

  @Override
  void initialize() {
    List<RoleWrapper> allRoles = Collections2.transform(RoleEntity.list(), new Function<RoleEntity, RoleWrapper>() {
      @Override
      RoleWrapper apply(RoleEntity entity) {
        return new RoleWrapper(RoleEntity.get(entity.id))
      }
    }) as List<RoleWrapper>
    roles = new BindingListModelList<RoleWrapper>(allRoles, true)
    roles.setMultiple(true)

    HashMap<String, Object> arg = Executions.getCurrent().getArg() as HashMap<String, Object>

    if (arg.size() > 0) {

      UserWrapper wrapper = arg.get("wrapper") as UserWrapper
      this.id = wrapper.getId()
      this.fio = wrapper.getFio()
      this.phone =  wrapper.getPhone()
      this.email = wrapper.getEmail()
      this.address = wrapper.getAddress()
      this.login = wrapper.getLogin()
      this.password = wrapper.getPassword()
      this.repassword = wrapper.getPassword()
      this.isActive = wrapper.isActive
      this.roles = wrapper.getRoles()

      String path = new PathBuilder()
          .appendPath(serverFoldersService.userPics)
          .appendString(wrapper.email)
          .build()
      String std_name = STD_FILE_NAMES.USER_NAME.getName()
      int std_size = STD_IMAGE_SIZES.MIDDLE.getSize()

      this.image = imageService.getImageFile(path, std_name, std_size)

    }

  }

  @Command
  public void saveUser() {

    UserEntity toSave = UserEntity.get(id)

    if (toSave == null) {
      toSave = new UserEntity()
      toSave.setEmail(email)
    }

    toSave.setLogin(login)
    toSave.setPassword(password.encodeAsSHA1())
    toSave.setAddress(address)
    toSave.setIsActive(isActive)

    UserEntity user = loginService.getCurrentUser()

    toSave.setActivateCode("created by::${user.login}")
    toSave.setFio(fio)
    toSave.setPhone(phone)

    roles.getSelection().each {it ->
      toSave.addToGroups(RoleEntity.get(it.id))
    }

    RoleEntity.list().each {role ->
      toSave.removeFromGroups(role)
    }

    if (toSave.validate())
      toSave.save(flush: true)

    //Тянем все группы.
    List<RoleEntity> allRoles = Collections2.transform(roles.getSelection(), new Function<RoleWrapper, RoleEntity>() {
      @Override
      RoleEntity apply(RoleWrapper wr) {
        return RoleEntity.get(wr.id)
      }
    }) as List<RoleEntity>

    allRoles.each {role ->
      toSave.addToGroups(role)
    }

    if (toSave.validate()) {
      toSave.save(flush: true)

      if (uuid != null) {
        File temp = new File(new PathBuilder()
            .appendPath(serverFoldersService.temp)
            .appendString(uuid)
            .build())

        File store = new File(new PathBuilder()
            .appendPath(serverFoldersService.userPics)
            .appendString(toSave.email)
            .build())
        if (!store.exists())
          store.mkdirs()

        FileUtils.copyDirectory(temp, store)
      }

    }
    Executions.sendRedirect("/admin/users")
  }

  @Command
  public void closeWnd(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Window wnd = event.getTarget().getSpaceOwner() as Window
    Window owner = wnd.getParent().getParent().getParent() as Window
    owner.detach()
  }


}
