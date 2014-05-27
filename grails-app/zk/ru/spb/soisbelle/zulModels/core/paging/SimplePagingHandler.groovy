package ru.spb.soisbelle.zulModels.core.paging

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.ExecutionArgParam
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import ru.spb.soisbelle.ImageStorageService
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.wrappers.ProductWrapper

/**
 * Created by gleb on 5/10/14.
 */
class SimplePagingHandler {

  //Все сущности
  List<ProductEntity> source = new ArrayList<ProductEntity>()
  //Отображаемые сущности
  List<ProductWrapper> currentData = new ArrayList<ProductWrapper>()

  // Шаг
  int step
  //Текущий индекс в списке всех товаров(allProducts)
  int currentPos
  //Показывать ли окно загрузки.
  boolean isBusy
  //Показывать или нет панель изменения вида отображения витрины.
  boolean isChangeShow = false
  //Показать или нет кнопку добавить.
  boolean endList = false
  //Показать или нет кнопку добавить.
  boolean startList = false

  public void init(List<ProductEntity> data, String isChangeShow, Long countPageItems) {

  }

  /**
   * Двигатьс по списку вперед на величину шага
   */
  private void next() {
    int oldPos = currentPos
    currentPos = currentPos + step
    int diff = source.size() - oldPos
    if (diff <= step) {
      endList = true
      startList = false
      refreshCurrentData(oldPos, oldPos + diff)
    } else {
      this.startList = false
      refreshCurrentData(oldPos, currentPos)
    }
  }

  /**
   * Двигаться по списку назад на величину шага
   */
  private void prev() {
    currentPos = currentPos - step
    if (currentPos == step) {
      startList = true
    }
    endList = false
    refreshCurrentData(currentPos - step, currentPos)
  }

  /**
   * Обновление данных покаываемых пользователю
   * @param start - начальная позиция всписке
   * @param end - конечная позиция в списке
   */
  void refreshCurrentData(int start, int end) {
    currentData.clear()
    source.subList(start, end).each { it ->
      currentData.add(new ProductWrapper(it))
    }
  }

}
