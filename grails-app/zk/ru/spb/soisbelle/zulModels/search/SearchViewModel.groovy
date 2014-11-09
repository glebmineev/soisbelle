package ru.spb.soisbelle.zulModels.search

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Sessions

class SearchViewModel {

  @Init
  public void init(){
  }

  @Command
  public void sendRequest(@BindingParam("request") String request){
    Sessions.getCurrent().setAttribute("keyword", request);
    Executions.sendRedirect("/search/index")
  }

}
