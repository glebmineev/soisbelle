package ru.spb.soisbelle.wrappers

import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.image.AImage
import org.zkoss.zkplus.databind.BindingListModelList
import org.zkoss.zul.ListModelList
import ru.spb.soisbelle.*
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES

class UserWrapper extends IdentWrapper implements Wrapper {

  String login
  String password

  String fio
  String phone
  String email
  String address

  ListModelList<RoleWrapper> roles

  AImage image
  UserWrapper memento
  boolean editingStatus = false

  ServerFoldersService serverFoldersService = ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService")
  ImageService imageService = ApplicationHolder.getApplication().getMainContext().getBean("imageService")

  UserWrapper() {}

  UserWrapper(UserEntity user) {
    this.id = user.id
    this.login = user.login
    this.password = null
    this.fio = user.fio
    this.phone = user.phone
    this.email = user.email
    this.address = user.address

    //Тянем все группы.
    List<RoleWrapper> allRoles = Collections2.transform(RoleEntity.list(), new Function<RoleEntity, RoleWrapper>() {
      @Override
      RoleWrapper apply(RoleEntity entity) {
        return new RoleWrapper(RoleEntity.get(entity.id))
      }
    }) as List<RoleWrapper>

    //Выделяем в листбоксе группы в которых состоит пользователь.
    List<RoleWrapper> userRoles = new ArrayList<RoleWrapper>()
    user.groups.each {it ->
      allRoles.each {fromAll ->
        if (it.id == fromAll.id)
          userRoles.add(fromAll)
      }
    }

    this.roles = new BindingListModelList<RoleWrapper>(allRoles, true)
    this.roles.setMultiple(true)
    this.roles.setSelection(userRoles)

    String path = new PathBuilder()
        .appendPath(serverFoldersService.userPics)
        .appendString(user.email)
        .build()
    String std_name = STD_FILE_NAMES.PROMO_NAME.getName()
    int std_size = STD_IMAGE_SIZES.MIDDLE.getSize()

    this.image = imageService.getImageFile(path, std_name, std_size)

  }

  public void restore(){
    this.id = memento.id
    this.name = memento.login
    this.password = null
    this.fio = memento.fio
    this.phone = memento.phone
    this.email = memento.email
    this.address = memento.address
    this.roles.clear()
    this.roles.addAll(memento.roles)
  }

  @Override
  @SuppressWarnings(value = "unchecked")
  protected UserWrapper clone(){
    UserWrapper cloned = new UserWrapper()
    cloned.setId(id)
    cloned.setName(name)
    cloned.setPassword(null)
    cloned.setFio(fio)
    cloned.setAddress(address)
    cloned.setEmail(email)
    cloned.setLogin(login)
    cloned.setPhone(phone)
    ListModelList<RoleWrapper> roles = new ListModelList<RoleWrapper>(roles)
    cloned.setRoles(roles)
    return cloned
  }

}
