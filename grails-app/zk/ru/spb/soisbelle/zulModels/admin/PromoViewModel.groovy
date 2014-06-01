package ru.spb.soisbelle.zulModels.admin

import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.Div
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Vlayout
import org.zkoss.zul.Window
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.PromoEntity
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.PromoWrapper
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

/**
 * Created by gleb on 31.05.14.
 */

@Init(superclass=true)
class PromoViewModel extends DownloadImageViewModel implements GrailsApplicationAware {

  GrailsApplication grailsApplication
  ListModelList<PromoWrapper> promosModel

  InitService initService

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.PROMO_NAME.getName()
    std_image_size = STD_IMAGE_SIZES.LARGEST.getSize()
    targetImage = ""
  }

  @Override
  void initialize() {

    initService = grailsApplication.getMainContext().getBean("initService")

    List<PromoWrapper> models = new ArrayList<PromoWrapper>()
    PromoEntity.list(sort: "name").each { it ->
      PromoWrapper model = new PromoWrapper(it)
      model.setMemento(model.clone() as PromoWrapper)
      models.add(model)
    }

    promosModel = new ListModelList<PromoWrapper>(models)
  }

  @Command
  public void changeEditableStatus(@BindingParam("model") PromoWrapper wrapper) {
    targetImage = wrapper.id
    wrapper.setEditingStatus(!wrapper.getEditingStatus())
    refreshRowTemplate(wrapper)
  }

  @Command
  public void refreshRowTemplate(PromoWrapper wrapper) {
    BindUtils.postNotifyChange(null, null, wrapper, "editingStatus");
  }

  @Command
  public void updatePromo(@BindingParam("model") PromoWrapper wrapper) {

    PromoEntity toSave = PromoEntity.get(wrapper.id)

    PromoEntity.withTransaction {

      File oldStore = new File(new PathBuilder()
          .appendPath(serverFoldersService.promoPics)
          .appendString("${toSave.id}_${toSave.getName()}")
          .build())

      toSave.setName(wrapper.getName())

      if (toSave.validate())
        toSave.save(flush: true)

      if (toSave.validate()) {
        toSave.save(flush: true)

        File store = new File(new PathBuilder()
            .appendPath(serverFoldersService.promoPics)
            .appendString("${toSave.id}_${toSave.getName()}")
            .build())

        if (!store.exists())
          store.mkdirs()

        if (uuid != null) {
          File temp = new File(new PathBuilder()
              .appendPath(serverFoldersService.temp)
              .appendString(uuid)
              .build())
          FileUtils.copyDirectory(temp, store)
        } else {
          FileUtils.copyDirectory(oldStore, store)
          FileUtils.deleteDirectory(oldStore)
        }

      }
    }

    changeEditableStatus(wrapper)

    initService.initPromos()

  }

  @Command
  @NotifyChange(["promosModel"])
  public void deletePromo(@BindingParam("model") PromoWrapper wrapper) {

    PromoEntity.withTransaction {
      PromoEntity toDelete = PromoEntity.get(wrapper.id)
      toDelete.delete(flush: true)
    }

    imageService.cleanStore(new File(new PathBuilder()
        .appendPath(serverFoldersService.promoPics)
        .appendString("${wrapper.id}_${wrapper.getName()}")
        .build()))

    List<PromoWrapper> models = new ArrayList<PromoWrapper>()
    PromoEntity.list(sort: "name").each { it ->
      PromoWrapper model = new PromoWrapper(it)
      model.setMemento(model.clone() as PromoWrapper)
      models.add(model)
    }

    promosModel.clear()
    promosModel.addAll(models)

    initService.initPromos()

  }

  @Command
  public void cancelEditing(@BindingParam("model") PromoWrapper wrapper) {
    wrapper.restore()
    changeEditableStatus(wrapper)
  }

  @Command
  public void createNew(){

    Window wnd = new Window()
    wnd.setWidth("80%")
    wnd.setHeight("500px")
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    Div div = new Div()
    div.setStyle("overflow: auto;")
    div.setWidth("100%")
    div.setHeight("500px")
    wnd.appendChild(div)
    Vlayout panel = new Vlayout()
    div.appendChild(panel)

    Map<Object, Object> params = new HashMap<Object, Object>()
    Executions.createComponents("/zul/admin/windows/createNewPromoWnd.zul", panel, params) as Window
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    wnd.doModal()
    wnd.setVisible(true)
  }

}
