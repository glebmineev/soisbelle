package ru.spb.soisbelle

import com.google.common.base.Strings
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.AfterCompose
import org.zkoss.zk.grails.composer.GrailsBindComposer
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Desktop
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.EventQueues
import ru.spb.soisbelle.wrappers.ProductWrapper
import ru.spb.soisbelle.zulModels.components.ShowcasePagingViewModel

@Deprecated
class ShowcaseBindPagingComposer extends GrailsBindComposer implements IShowcaseComposer {

  ShowcaseService showcaseService = ApplicationHolder.getApplication().getMainContext().getBean("showcaseService") as ShowcaseService

  ShowcaseBindPagingComposer() {
    //динамически добавляем метод runAsync при конструировании класса компоузера.
    ShowcaseBindPagingComposer.class.metaClass.runAsync = { Runnable runme ->
      ApplicationHolder.getApplication().getMainContext().executorService.withPersistence(runme)
    }
  }

  @AfterCompose
  void doAfterCompose(Component component){
    if (Strings.isNullOrEmpty(component.getId()) && !"catalog".equals(component.getId())) {
    Desktop desktop = Executions.getCurrent().getDesktop()
    desktop.enableServerPush(true)
    ShowcasePagingViewModel showcaseViewModel = getViewModel() as ShowcasePagingViewModel
    runAsync {
      showcaseService.setComposer(this)
      showcaseService.setDesktop(desktop)
      showcaseService.processEntities(showcaseViewModel.getAllProducts(), showcaseViewModel.getStep())
    }
    }
  }

  @Override
  void complete(List<ProductWrapper> data) {
    Map<String, Object> args = new HashMap<String, Object>()
    args.put("data", data)
    BindUtils.postGlobalCommand("showcasepagingqueue", EventQueues.DESKTOP, "refreshShowcase", args);
  }
}
