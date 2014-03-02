package ru.spb.locon.windows

import org.zkoss.image.AImage
import org.zkoss.zul.Window
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Button
import org.zkoss.zk.ui.event.Events
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Vbox
import org.zkoss.zul.Hbox

/**
 * User: Gleb
 * Date: 11.11.12
 * Time: 17:59
 */
class ImageWindow extends Window {

  ImageWindow(AImage aImage, String name) {

    setWidth("520px")
    setHeight("560px")

    Vbox vbox = new Vbox()
    vbox.setWidth("520px")
    vbox.setAlign("center")

    Label label = new Label(name)
    Image close = new Image("/images/failed.png")
    close.addEventListener(Events.ON_CLICK, new EventListener(){
      @Override
      void onEvent(Event t) {
        setVisible(false)
      }
    })
    Image productImage = new Image()
    productImage.setContent(aImage)

    vbox.appendChild(label)
    vbox.appendChild(productImage)
    vbox.appendChild(close)

    appendChild(vbox)
  }
}
