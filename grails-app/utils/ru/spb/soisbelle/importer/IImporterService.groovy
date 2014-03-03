package ru.spb.soisbelle.importer

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.hibernate.SessionFactory
import org.zkoss.zk.ui.Desktop
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.IImporterComposer
import ru.spb.soisbelle.ImageService
import ru.spb.soisbelle.InitService
import ru.spb.soisbelle.zulModels.importer.ImportEvent

/**
 * User: Gleb
 * Date: 02.02.13
 * Time: 20:35
 */
abstract class IImporterService {

  ImageService imageService = ApplicationHolder.getApplication().getMainContext().getBean("imageService") as ImageService
  def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
  SessionFactory sessionFactory = ApplicationHolder.getApplication().getMainContext().getBean("sessionFactory") as SessionFactory
  InitService initService = ApplicationHolder.getApplication().getMainContext().getBean("initService") as InitService

  Desktop desktop
  IImporterComposer composer

  abstract void doImport(InputStream is)

  /**
   * Очищает гибернейтовскую сессию.
   * @return
   */
  def cleanUpGorm() {
    def session = sessionFactory.currentSession
    session.flush()
    session.clear()
    propertyInstanceMap.get().clear()
  }

  void sendSuccessfulEvent(ImportEvent event) {
    addRow(event)
  }

  void sendErrorEvent(ImportEvent event) {
    addRow(event)
  }

  void addRow(ImportEvent event) {
    Executions.activate(desktop)
    composer.addRow(event)
    Executions.deactivate(desktop)
  }

  //отсыл события прогрессметеру и инфо лебелу композера IImportComposer.
  void doProgress(int value, String message){
    Executions.activate(desktop)
    composer.doProgress(value, message)
    Executions.deactivate(desktop)
  }

  //отсыл события завершения импорта успешно композера IImportComposer.
  void complete(String message) {
    Executions.activate(desktop)
    composer.complete(message)
    Executions.deactivate(desktop)
  }

}
