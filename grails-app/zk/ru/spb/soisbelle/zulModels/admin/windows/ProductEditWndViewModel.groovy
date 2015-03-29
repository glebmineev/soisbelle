package ru.spb.soisbelle.zulModels.admin.windows

import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Window
import ru.spb.soisbelle.CategoryEntity
import ru.spb.soisbelle.FilterEntity
import ru.spb.soisbelle.ImageService
import ru.spb.soisbelle.ManufacturerEntity
import ru.spb.soisbelle.ProductEntity
import ru.spb.soisbelle.ServerFoldersService
import ru.spb.soisbelle.common.CategoryPathHandler
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.importer.ConverterRU_EN
import ru.spb.soisbelle.zulModels.core.DownloadImageViewModel

/**
 * Модель формы редактирования товара.
 */
@Init(superclass=true)
class ProductEditWndViewModel extends DownloadImageViewModel {

  Long productID

  //Комбобоксы
  ListModelList<FilterEntity> filterModel
  ListModelList<ManufacturerEntity> manufacturerModel

  ManufacturerEntity selectedManufacturer
  FilterEntity selectedFilter

  AImage image

  //Простые поля.
  String article
  String name
  String description
  String price
  String volume

  //Отображение компонентов в случае если диалоговое окно
  boolean isModal

  ImageService imageService = ApplicationHolder.getApplication().getMainContext().getBean("imageService");

  ServerFoldersService serverFoldersService = ApplicationHolder.getApplication().getMainContext().getBean("serverFoldersService") as ServerFoldersService

  @Override
  void downloadParams() {
    std_name = STD_FILE_NAMES.PRODUCT_NAME.getName()
    targetImage = "targetImage"
    std_image_size = STD_IMAGE_SIZES.MIDDLE.getSize()
  }

  @Override
  void initialize() {
    HashMap<String, Object> arg = Executions.getCurrent().getArg() as HashMap<String, Object>

    if (arg.size() > 0) {
      productID = arg.get("product") as Long
      isModal = true
    }

    if (productID != null)
      initItem(ProductEntity.get(productID))

    initComboboxes()
  }

  public void initComboboxes() {
    List<FilterEntity> filters = new ArrayList<FilterEntity>()
    collectAllFilters(ProductEntity.get(productID).getEndCategory(), filters)
    filterModel = new ListModelList<FilterEntity>(filters)
    manufacturerModel = new ListModelList<ManufacturerEntity>(ManufacturerEntity.list(sort: "name"))
  }

  void collectAllFilters(CategoryEntity category, List<FilterEntity> filters) {
    List<CategoryEntity> categories = category.listCategory as List<CategoryEntity>
    if (categories != null && categories.size() > 0)
      categories.each { CategoryEntity it ->
        if (it.listCategory != null && it.listCategory.size() > 0)
          collectAllFilters(it, filters)
        else
          filters.addAll(it.filters as List<FilterEntity>)
      }
    else
      filters.addAll(category.filters as List<FilterEntity>)
  }

  public void initItem(ProductEntity product) {
    article = product.article
    name = product.name
    description = product.description
    price = product.price
    volume = product.volume

    selectedManufacturer = product.manufacturer
    selectedFilter = product.filter

    String path = new PathBuilder()
        .appendPath(serverFoldersService.productImages)
        .appendString(product.engImagePath)
        .build()
    String std_name = STD_FILE_NAMES.PRODUCT_NAME.getName()
    int std_size = STD_IMAGE_SIZES.MIDDLE.getSize()

    this.image = imageService.getImageFile(path, std_name, std_size)

  }


  @Command
  public void saveItem() {
    ProductEntity product = ProductEntity.get(productID)
    updateProduct(product)

    Executions.sendRedirect("/admin/products")
  }

  /**
   * Апдейт продукта.
   * @param product
   */
  public void updateProduct(ProductEntity product) {

    ProductEntity.withTransaction {
      product.setName(name)
      product.setArticle(article)
      product.setManufacturer(selectedManufacturer)
      product.setDescription(description)
      product.setFilter(selectedFilter)
      //если валидно копируем файл картинки из темповой дериктории.
      if (product.validate()) {

        String oldPath = product.engImagePath
        File src = new File(new PathBuilder()
            .appendPath(serverFoldersService.productImages)
            .appendString(oldPath).build())

        if (uuid != null)
          src = new File(new PathBuilder()
              .appendPath(serverFoldersService.temp)
              .appendString(uuid).build())

        String dirPath = generateProductImagePath()
        String filePath = new PathBuilder()
            .appendPath(dirPath)
            .appendString("${article}_${name}").build()
        String translit = ConverterRU_EN.translit("${filePath}")

        product.setImagePath(filePath)
        product.setEngImagePath(translit)
        product.save(flush: true)

        File store = new File(new PathBuilder()
            .appendPath(serverFoldersService.productImages)
            .appendString(translit)
            .checkDir()
            .build())

        FileUtils.copyDirectory(src, store)
        imageService.cleanStore(src)
      }
    }
  }

  /**
   * Формируем путь до файла с изображением.
   * @return - путь в файловой системе.
   */
  public String generateProductImagePath() {

    CategoryEntity category = ProductEntity.get(productID).getEndCategory()
    List<CategoryEntity> categories = CategoryPathHandler.getCategoryPath(category)

    String imagePath = new PathBuilder()
        .appendPath(categories.first().name)
        .appendString(selectedManufacturer.name).build()

    CategoryEntity[] array = categories.toArray()
    for (int i = 1; i < array.length; i++)
      imagePath = new PathBuilder()
          .appendPath(imagePath)
          .appendString(array[i].name)

    return imagePath

  }

  @Command
  public void closeWnd(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
    Window wnd = event.getTarget().getSpaceOwner() as Window
    Window owner = wnd.getParent().getParent().getParent() as Window
    owner.detach()
  }

}
