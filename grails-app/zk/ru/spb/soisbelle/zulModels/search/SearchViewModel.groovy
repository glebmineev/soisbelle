package ru.spb.soisbelle.zulModels.search

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Sessions
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.PathBuilder

class SearchViewModel {

  ServerFoldersService serverFoldersService =
      ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  AImage searchButtonImage

  @Init
  public void init(){
    searchButtonImage = new AImage(new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("dsn")
        .appendPath("header")
        .appendString("search_button.png")
        .build())
  }

  @Command
  public void sendRequest(@BindingParam("request") String request){
    Sessions.getCurrent().setAttribute("keyword", request);
    Executions.sendRedirect("/search/index")
  }

}
