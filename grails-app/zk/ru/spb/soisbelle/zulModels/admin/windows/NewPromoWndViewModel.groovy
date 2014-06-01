package ru.spb.soisbelle.zulModels.admin.windows

import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Window
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.PromoEntity
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

/**
 * Created by gleb on 31.05.14.
 */
@Init(superclass=true)
class NewPromoWndViewModel extends DownloadImageViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication

  InitService initService

  //Логгер
  static Logger log = LoggerFactory.getLogger(ManufacturerWndViewModel.class)

  String name

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.USER_NAME.getName()
    std_image_size = STD_IMAGE_SIZES.LARGEST.getSize()
    targetImage = "targetImage"
  }

  @Override
  void initialize() {
    initService = grailsApplication.getMainContext().getBean("initService")
  }

  @Command
  public void savePromo() {

    PromoEntity toSave = new PromoEntity()
    toSave.setName(name)

    if (toSave.validate()) {
      toSave.save(flush: true)

      if (uuid != null) {
        File temp = new File(new PathBuilder()
            .appendPath(serverFoldersService.temp)
            .appendString(uuid)
            .build())

        File store = new File(new PathBuilder()
            .appendPath(serverFoldersService.promoPics)
            .appendString("${toSave.id}_${toSave.getName()}")
            .build())
        if (!store.exists())
          store.mkdirs()

        FileUtils.copyDirectory(temp, store)
      }

    }
    Executions.sendRedirect("/admin/promo")
    initService.initPromos()

  }

  @Command
  public void closeWnd(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Window wnd = event.getTarget().getSpaceOwner() as Window
    Window owner = wnd.getParent().getParent().getParent() as Window
    owner.detach()
  }


}
