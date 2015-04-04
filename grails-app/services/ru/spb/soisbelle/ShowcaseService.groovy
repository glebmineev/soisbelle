package ru.spb.soisbelle

import com.google.common.base.Function
import com.google.common.collect.Collections2
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.zkoss.zk.ui.Desktop
import org.zkoss.zk.ui.Executions
import ru.spb.soisbelle.wrappers.ProductWrapper

/**
 * Сервис для отложенной обработки товаров, для их корректного отображения.
 */
class ShowcaseService implements ApplicationContextAware{

  static transactional = true
  static scope = "session";
  static proxy = true

  ApplicationContext applicationContext
  IShowcaseComposer composer
  Desktop desktop

  CartService cartService

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
    Collection<ProductWrapper> transformed = Collections2.transform(list, new Function<ProductEntity, ProductWrapper>() {
      @Override
      ProductWrapper apply(ProductEntity f) {
        ProductWrapper wrapper = new ProductWrapper(f)
        cartService.initAsCartItem(wrapper)
        return wrapper;
      }
    }) as List<ProductWrapper>

    sendCompleteEvent(transformed)
  }

  void sendCompleteEvent(Collection<ProductWrapper> transformed){
    Executions.activate(desktop)
    composer.complete(transformed)
    Executions.deactivate(desktop)
  }


}
