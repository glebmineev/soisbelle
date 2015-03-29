package ru.spb.soisbelle.zulModels.admin

import com.google.common.base.Strings
import org.zkoss.bind.BindUtils
import org.zkoss.bind.Binder
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.GlobalCommand
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.Events
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zk.ui.util.Clients
import org.zkoss.zul.*
import org.zkoss.zul.impl.XulElement
import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.CategoryTreeNode
import ru.spb.soisbelle.wrappers.ProductWrapperSimple
import ru.spb.soisbelle.zulModels.admin.filters.data.FilterBean
import ru.spb.soisbelle.zulModels.admin.filters.data.FilterTypes
import ru.spb.soisbelle.zulModels.admin.filters.IFilterCallback
import ru.spb.soisbelle.zulModels.admin.filters.data.ObjectFilter
import ru.spb.soisbelle.annotation.FieldInfo
import ru.spb.soisbelle.wrappers.ProductWrapper

import java.lang.reflect.Field
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 4/6/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
class ProductsViewModel {

  //Фильтры (поле, значение)
  Map<Field, Object> filterMap = new HashMap<Field, Object>()
  List<FilterBean> filters = new ArrayList<FilterBean>()
  //Модель гриды.
  ListModelList<ProductWrapperSimple> products = new ListModelList<ProductWrapperSimple>()
  ListModelList<ProductEntity> allProducts = new ListModelList<ProductEntity>()

  // Показывать ли окно загрузки.
  boolean isBusy

  Binder binder

  @Init
  public void init(@ContextParam(ContextType.BINDER) Binder binder) {

    ProductEntity.declaredFields.each { Field field ->
      FieldInfo annotation = field.getAnnotation(FieldInfo)
      if (annotation != null && annotation.isFilter()) {

        FilterBean bean = new FilterBean(new IFilterCallback() {
          @Override
          void changed(ObjectFilter objectFilter, XulElement link) {
            filterMap.put(objectFilter.field, objectFilter)
            rebuildModel(link)
          }
        }, field, annotation.type())

        filters.add(bean)

      }

    }
    this.binder = binder

    this.isBusy = true

    //rebuildModel()
    products.clear()
    allProducts = ProductEntity.list(sort: "name")
    products.setMultiple(true)
  }

  @GlobalCommand
  @NotifyChange(["products", "isBusy"])
  public void refreshGrid(@BindingParam("data") List<ProductWrapperSimple> data) {
    products.clear()
    products.addAll(data)
    this.isBusy = false
  }

  /**
   * Формирует окошко с увеличением цены товаров.
   * @param event
   */
  @Command
  public void changePrice() {

    Map<Object, Object> params = new HashMap<Object, Object>()
    params.put("selectedProducts", products.getSelection())
    Window wnd = Executions.createComponents("/zul/admin/windows/incrementProductsWnd.zul", null, params) as Window
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    wnd.doModal()
    wnd.setVisible(true)

  }

  @Command
  public void changeEditableStatus(@BindingParam("wrapper") ProductWrapperSimple wrapper) {
    wrapper.setEditingStatus(!wrapper.getEditingStatus())
    refreshRowTemplate(wrapper);
  }

  @Command
  public void refreshRowTemplate(ProductWrapperSimple wrapper) {
    BindUtils.postNotifyChange("productgridbindqueue", null, wrapper, "editingStatus");
  }

  @Command
  public void deleteProduct(@BindingParam("wrapper") ProductWrapperSimple wrapper) {

    Messagebox.show("Удалить?", "Удаление категории", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
      public void onEvent(Event evt) throws InterruptedException {
        if (evt.getName().equals("onOK")) {
          ProductEntity.withTransaction {
            ProductEntity toDelete = ProductEntity.get(wrapper.getId())
            toDelete.delete(flush: true)
          }
          rebuildData()
        }
      }
    });

  }

  @Command
  public void editProduct(@BindingParam("wrapper") ProductWrapperSimple wrapper){
    Window wnd = new Window()
    wnd.setStyle("border: 1px solid #643845; background: #BECAD6;")
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

    Map<String, Object> params = new HashMap<String, Object>()
    params.put("product", wrapper.id)
    Executions.createComponents("/zul/admin/windows/productEditWnd.zul", panel, params)
    wnd.doModal()
    wnd.setVisible(true)
  }

/*  @Command
  public void cancelEditing(@BindingParam("wrapper") ProductWrapperSimple wrapper) {
    wrapper.restore()
    changeEditableStatus(wrapper)
  }*/

  ExecutorService service = Executors.newScheduledThreadPool(2);

  @Command
  @NotifyChange(["products", "isBusy"])
  public void rebuildData() {

    this.isBusy = false
    this.products.clear()
    this.products.addAll(fillWrappers(getModelByFilters()))
    BindUtils.postNotifyChange("productgridbindqueue", null, this, "isBusy")
/*    service.execute(new Runnable() {

      @Override
      void run() {
        int i = 0

        isBusy = false
        refreshGrid2(fillWrappers(ProductEntity.list(max: 5)))
      }

    })*/
    int r = 0
  }

  @Command
  @NotifyChange(["products", "isBusy"])
  public void rebuildModel(XulElement link) {
    int t = 6

    this.isBusy = true
    BindUtils.postNotifyChange("productgridbindqueue", null, this, "isBusy")

    this.products.clear()
    //products.addAll(fillWrappers(getModelByFilters()))

    link.addEventListener("onEcho", new org.zkoss.zk.ui.event.EventListener<Event>() {

      public void onEvent(Event event) throws Exception {
        rebuildData()
        link.removeEventListener("onEcho", this)
      }
    });
    Events.echoEvent("onEcho", link, null)

  }

  /**
   * Метод создает обертки на основе сущностей.
   * @param list - список оригиналов.
   * @return список оберток.
   */
  List<ProductWrapperSimple> fillWrappers(def list) {
    List<ProductWrapperSimple> target = new ArrayList<ProductWrapperSimple>()
    list.each { ProductEntity it ->
      ProductWrapperSimple wrapper = new ProductWrapperSimple(it)
      wrapper.setEditingStatus(false)
      target.add(wrapper)
    }
    return target
  }

  List<ProductEntity> getModelByFilters() {
    return ProductEntity.createCriteria().list {
      filterMap.each { Field field, ObjectFilter filter ->

        FieldInfo annotation = field.getAnnotation(FieldInfo)

        if (annotation.type() == FilterTypes.TEXT_FIELD &&
            Strings.emptyToNull(filter.startValue as String))
          ilike(field.name, "%${(String) filter.startValue}%")

        if (annotation.type() == FilterTypes.COMBO_FIELD &&
            filter.startValue != null)
          sqlRestriction("product_${field.name}_id = '${filter.startValue.id}'")

        if (annotation.type() == FilterTypes.NUMBER_FIELD &&
            Strings.emptyToNull(filter.startValue as String))
          sqlRestriction("cast(product_${field.name} as varchar) like '%${(String) filter.startValue}%'")

        if (annotation.type() == FilterTypes.MEASURE_FIELD) {
          if (Strings.emptyToNull(filter.startValue as String))
            gt("${field.name}", filter.startValue as Long)
          if (Strings.emptyToNull(filter.endValue as String))
            le("${field.name}", filter.endValue as Long)

        }

      }
    }
  }

}
