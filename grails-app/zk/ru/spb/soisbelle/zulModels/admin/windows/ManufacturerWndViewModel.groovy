package ru.spb.soisbelle.zulModels.admin.windows

import com.google.common.base.Strings
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Window
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.ManufacturerWrapper
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

class ManufacturerWndViewModel extends DownloadImageViewModel {

  //Логгер
  static Logger log = LoggerFactory.getLogger(ManufacturerWndViewModel.class)

  Long id
  String name
  String shortName
  String description
  AImage image

  @Init(superclass=true)
  public void initMWnd(){ }

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.PRODUCT_NAME.getName()
    std_image_size = STD_IMAGE_SIZES.SMALLEST.getSize()
    targetImage = "targetImage"
  }

  @Override
  void initialize() {
    HashMap<String, Object> arg = Executions.getCurrent().getArg() as HashMap<String, Object>

    if (arg.size() > 0) {

      ManufacturerWrapper wrapper = arg.get("wrapper") as ManufacturerWrapper
      this.id = wrapper.getId()
      this.name = wrapper.getName()
      this.shortName =  wrapper.getShortName()
      this.description = wrapper.getDescription()
      this.image = wrapper.getImage()

    }

  }

  @Command
  public void saveManufacturer(){
    ManufacturerEntity.withTransaction {
      ManufacturerEntity toSave = ManufacturerEntity.get(id)
      if (toSave == null) {
        toSave = new ManufacturerEntity()
      }

      if (Strings.isNullOrEmpty(toSave.getPicUuid())){
        toSave.setPicUuid(UUID.randomUUID().toString())
      }

      toSave.setName(name)
      toSave.setShortName(shortName)
      toSave.setDescription(description)
      if (toSave.validate()) {
        toSave.save(flush: true)

        if (uuid != null) {
          File temp = new File(new PathBuilder()
              .appendPath(serverFoldersService.temp)
              .appendString(uuid)
              .build())
          File store = new File(new PathBuilder()
              .appendPath(serverFoldersService.manufacturersPics)
              .appendString(toSave.picUuid)
              .build())
          if (!store.exists())
            store.mkdirs()

          FileUtils.copyDirectory(temp, store)
        }

      }
    }
    Executions.sendRedirect("/admin/manufacturers")
  }

  @Command
  public void closeWnd(@ContextParam(ContextType.TRIGGER_EVENT) Event event){
    try {
      Window wnd = event.getTarget().getSpaceOwner() as Window
      Window owner = wnd as Window
      owner.detach()
    } catch (Exception ex) {
      log.debug(ex.getMessage())
    }
  }


}
