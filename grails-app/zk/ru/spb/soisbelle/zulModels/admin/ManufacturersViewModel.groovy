package ru.spb.soisbelle.zulModels.admin

import com.google.common.base.Strings
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Window
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.ManufacturerWrapper
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

@Init(superclass=true)
class ManufacturersViewModel extends DownloadImageViewModel {

  ListModelList<ManufacturerWrapper> manufacturersModel
  AImage pencil
  AImage failed

  ImageStorageService imageStorageService = ApplicationHolder.getApplication().getMainContext().getBean("imageStorageService") as ImageStorageService

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.MANUFACTURER_NAME.getName()
    std_image_size = STD_IMAGE_SIZES.SMALLEST.getSize()
    targetImage = ""
  }

  @Override
  void initialize() {
    List<ManufacturerWrapper> models = new ArrayList<ManufacturerWrapper>()
    ManufacturerEntity.list(sort: "name").each { it ->
      ManufacturerWrapper model = new ManufacturerWrapper(it)
      model.setMemento(model.clone())
      models.add(model)
    }

    this.manufacturersModel = new ListModelList<ManufacturerWrapper>(models)

    this.pencil = imageStorageService.pencil
    this.failed = imageStorageService.failed

  }

/*  @Command
  public void changeEditableStatus(@BindingParam("changeStatusWrapper") ManufacturerWrapper wrapper) {
    targetImage = wrapper.id
    wrapper.setEditingStatus(!wrapper.getEditingStatus())
    refreshRowTemplate(wrapper)
  }

  public void refreshRowTemplate(ManufacturerWrapper wrapper) {
    BindUtils.postNotifyChange(null, null, wrapper, "editingStatus");
  }*/

  @Command
  public void updateManufacturer(@BindingParam("changeStatusWrapper") ManufacturerWrapper wrapper) {

    ManufacturerEntity.withTransaction {
      ManufacturerEntity toSave = ManufacturerEntity.get(wrapper.id)
      toSave.setName(wrapper.getName())
      toSave.setShortName(wrapper.shortName)
      toSave.setDescription(wrapper.description)
      if (toSave.validate()) {
        toSave.save(flush: true)

        if (uuid != null) {
          File temp = new File(new PathBuilder()
              .appendPath(serverFoldersService.temp)
              .appendString(uuid)
              .build())

          File store = new File(new PathBuilder()
              .appendPath(serverFoldersService.manufacturersPics)
              .appendString(wrapper.name)
              .build())
          if (!store.exists())
            store.mkdirs()

          FileUtils.copyDirectory(temp, store)
        }

      }
    }

    //changeEditableStatus(wrapper)
  }

  @Command
  @NotifyChange(["manufacturersModel"])
  public void deleteManufacturer(@BindingParam("wrapper") ManufacturerWrapper wrapper) {

    ManufacturerEntity.withTransaction {
      ManufacturerEntity toDelete = ManufacturerEntity.get(wrapper.id)
      toDelete.delete(flush: true)
    }

    imageService.cleanStore(new File(new PathBuilder()
        .appendPath(serverFoldersService.manufacturersPics)
        .appendString(wrapper.name)
        .build()))

    List<ManufacturerWrapper> models = new ArrayList<ManufacturerWrapper>()
    ManufacturerEntity.list(sort: "name").each { it ->
      ManufacturerWrapper model = new ManufacturerWrapper(it)
      model.setMemento(model.clone() as ManufacturerWrapper)
      models.add(model)
    }

    manufacturersModel.clear()
    manufacturersModel.addAll(models)

  }

  @Command
  public void cancelEditing(@BindingParam("changeStatusWrapper") ManufacturerWrapper wrapper) {
    wrapper.restore()
    //changeEditableStatus(wrapper)
  }

  @Command
  public void createNew(){
    Map<Object, Object> params = new HashMap<Object, Object>()
    Window wnd = Executions.createComponents("/zul/admin/windows/manufacturerWnd.zul", null, params) as Window
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    wnd.doModal()
    wnd.setVisible(true)
  }

  @Command
  public void editManufacturer(@BindingParam("wrapper") ManufacturerWrapper wrapper){
    Map<Object, Object> params = new HashMap<Object, Object>()
    params.put("wrapper", wrapper)
    Window wnd = Executions.createComponents("/zul/admin/windows/manufacturerWnd.zul", null, params) as Window
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    wnd.doModal()
    wnd.setVisible(true)
  }

}
