package ru.spb.soisbelle.wrappers

import ru.spb.soisbelle.FilterEntity

class FilterWrapper implements Wrapper {

  Long id
  String name

  FilterWrapper memento
  boolean editingStatus = false

  FilterWrapper(FilterEntity entity){
    this.id = entity.id
    this.name = entity.name
  }

  public void restore(){
    this.name = memento.name
  }

}
