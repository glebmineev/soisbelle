package ru.spb.soisbelle.zulModels.shop.windows

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Window
import ru.spb.soisbelle.ImageStorageService

/**
 * Created by gleb on 15.05.14.
 */
class ZoomImageWndViewModel {

  AImage image
  AImage closeWndImage

  ImageStorageService imageStorageService =
      ApplicationHolder.getApplication().getMainContext().getBean("imageStorageService") as ImageStorageService

  @Init
  public void init(){
    HashMap<String, Object> arg = Executions.getCurrent().getArg() as HashMap<String, Object>
    image = arg.get("image") as AImage
    closeWndImage = imageStorageService.closeWnd
  }

  @Command
  public void closeWnd(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Window wnd = event.getTarget().getSpaceOwner() as Window
    Window owner = wnd as Window
    owner.detach()
  }

}
