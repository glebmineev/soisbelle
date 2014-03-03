package ru.spb.soisbelle

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command

/**
 * Created with IntelliJ IDEA.
 * User: Gleb-PC
 * Date: 10.02.13
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
class TestsComposer /*extends GrailsComposer*/  {

/*  @Override
  public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);
  }*/

  @Command
  public void test(@BindingParam("value") String value){
    int r = 0;
/*    ForwardEvent eventx = (ForwardEvent) event;
    println(eventx.getOrigin().getData())*/
  }

}
