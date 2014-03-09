package ru.spb.soisbelle.wrappers

import com.google.common.base.Strings
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.image.AImage
import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.PathBuilder

class CategoryWrapper implements Wrapper {

  Long id
  String name
  String parentName
  String description
  AImage menuImage

  List<CategoryWrapper> children = new ArrayList<CategoryWrapper>()

  CategoryWrapper(CategoryEntity entity) {
    this.id = entity.id
    this.name = entity.name
    this.parentName = entity.parentCategory != null ? entity.parentCategory.name : ""

    String translitName = entity.translitName

    if (!Strings.isNullOrEmpty(translitName)) {
      getImageOnlyRootCategories(translitName)
    }

  }

  private void getImageOnlyRootCategories(String name){

    ServerFoldersService serverFoldersService =
        ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

    PathBuilder builder = new PathBuilder()
        .appendPath(serverFoldersService.images)
        .appendPath("dsn")
        .appendPath("menu")
        .appendString(name)
        .appendExt("png")

    String path = builder.build()

    File imageFile = new File(path)
    if (imageFile.exists())
      menuImage = new AImage(path)
  }

  public void addChild(CategoryWrapper wrapper){
    children.add(wrapper)
  }

  @Override
  void restore() {

  }

}
