package ru.spb.soisbelle

import com.google.common.base.Function
import com.google.common.collect.Collections2
import grails.transaction.Transactional
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.zkoss.zk.ui.Desktop
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.wrappers.ProductWrapper
import ru.spb.soisbelle.wrappers.ProductWrapperSimple

@Transactional
class GridService  implements ApplicationContextAware {

  static transactional = true
  static scope = "session";
  static proxy = true

  ApplicationContext applicationContext
  IShowcaseComposer composer
  Desktop desktop

  /**
   * Обработка данных для модели с страницами
   * @param data - данные
   * @param step - количество отображаемых элементов на странице
   */
  void processEntities(List<ProductEntity> data, int step){

    List<ProductEntity> list = data.subList(0, data.size() >= step ? step : data.size())
    process(list)
  }

  /**
   * Обработка данных для простой модели
   * @param data
   */
  void processEntities(List<ProductEntity> data) {
    process(data)
  }

  /**
   * Преобразование данных их сущностей в обертки
   * @param list - список сущностей
   */
  private void process(List<ProductEntity> list) {
    Collection<ProductWrapperSimple> transformed = Collections2.transform(list, new Function<ProductEntity, ProductWrapperSimple>() {
      @Override
      ProductWrapperSimple apply(ProductEntity f) {
        ProductWrapperSimple wrapper = new ProductWrapperSimple(f)
        return wrapper;
      }
    }) as List<ProductWrapper>

    sendCompleteEvent(transformed)
  }

  void sendCompleteEvent(Collection<ProductWrapperSimple> transformed){
    Executions.activate(desktop)
    composer.complete(transformed)
    Executions.deactivate(desktop)
  }

}
