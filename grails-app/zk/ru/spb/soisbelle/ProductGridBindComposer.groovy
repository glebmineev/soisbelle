package ru.spb.soisbelle

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.AfterCompose
import org.zkoss.zk.grails.composer.GrailsBindComposer
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Desktop
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.EventQueues
import ru.spb.soisbelle.wrappers.ProductImageryWrapper
import ru.spb.soisbelle.wrappers.ProductWrapper
import ru.spb.soisbelle.zulModels.admin.ProductsViewModel

/**
 * Created by gleb on 28.03.2015.
 */
class ProductGridBindComposer extends GrailsBindComposer implements IShowcaseComposer {
  GridService gridService = ApplicationHolder.getApplication().getMainContext().getBean("gridService") as GridService

  ProductGridBindComposer() {
    //динамически добавляем метод runAsync при конструировании класса компоузера.
    ProductGridBindComposer.class.metaClass.runAsync = { Runnable runme ->
      ApplicationHolder.getApplication().getMainContext().executorService.withPersistence(runme)
    }
  }

  @AfterCompose
  void doAfterCompose(Component component){
    super.doAfterCompose(component)
    //if (Strings.isNullOrEmpty(component.getId()) && !"editorWnd".equals(component.getId())) {
      Desktop desktop = Executions.getCurrent().getDesktop()
      desktop.enableServerPush(true)
      ProductsViewModel productViewModel = getViewModel() as ProductsViewModel
      runAsync {
        gridService.setComposer(this)
        gridService.setDesktop(desktop)
        gridService.processEntities(productViewModel.getAllProducts(), productViewModel.getAllProducts().size())
      }
    //}
  }

  @Override
  void complete(List<ProductImageryWrapper> data) {
    Map<String, Object> args = new HashMap<String, Object>()
    args.put("data", data)
    BindUtils.postGlobalCommand("productgridbindqueue", EventQueues.DESKTOP, "refreshGrid", args);
  }

}
