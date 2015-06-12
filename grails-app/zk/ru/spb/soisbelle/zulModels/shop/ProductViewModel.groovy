package ru.spb.soisbelle.zulModels.shop

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.slf4j.*
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.image.AImage
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.sys.ExecutionsCtrl
import org.zkoss.zul.Window
import ru.spb.soisbelle.*
import ru.spb.soisbelle.common.CategoryPathHandler
import ru.spb.soisbelle.common.PathBuilder
import ru.spb.soisbelle.common.STD_FILE_NAMES
import ru.spb.soisbelle.common.STD_IMAGE_SIZES
import ru.spb.soisbelle.wrappers.HrefWrapper
import ru.spb.soisbelle.wrappers.ProductImageryWrapper
import ru.spb.soisbelle.wrappers.ReviewWrapper

class ProductViewModel implements GrailsApplicationAware {

  //Логгер
  static Logger log = LoggerFactory.getLogger(ProductViewModel.class)

  GrailsApplication grailsApplication

  Long productId
  List<ReviewWrapper> reviews = new ArrayList<ReviewWrapper>()

  //Навигация.
  List<HrefWrapper> links = new LinkedList<HrefWrapper>()

  ProductImageryWrapper model

  CartService cartService
  ImageService imageService
  ServerFoldersService serverFoldersService

  @Init
  public void init() {
    productId = Executions.getCurrent().getParameter("product") as Long
    if (productId != null) {
      initServices()
      initGrid()
      initReviews()
      buildProductNavPath(productId)
    }
  }

  public void buildProductNavPath(Long productID) {
    ProductEntity product = ProductEntity.get(productID)
    rebuildCategoryNavPath(product.getEndCategory().id)
    links.add(new HrefWrapper(product.name, "/shop/product?product=${product.id}"))
  }

  /**
   * Формирование навигации.
   * @param categoryID - текущщая категория.
   */
  void rebuildCategoryNavPath(Long categoryID) {
    List<CategoryEntity> categories = CategoryPathHandler.getCategoryPath(CategoryEntity.get(categoryID))
    links.clear()
    links.add(new HrefWrapper("Главная", "/shop"))
    categories.each { it ->
      links.add(new HrefWrapper(it.name, "/shop/catalog/category?category=${it.id}"))
    }
  }

  public void initServices(){
    cartService = grailsApplication.getMainContext().getBean("cartService")
    imageService = grailsApplication.getMainContext().getBean("imageService")
    serverFoldersService = grailsApplication.getMainContext().getBean("serverFoldersService")
  }

  public void initGrid() {
    model = new ProductImageryWrapper(ProductEntity.get(productId), STD_IMAGE_SIZES.MIDDLE.getSize())
    cartService.initAsCartItem(model)
  }

  public void initReviews() {
    ProductEntity product = ProductEntity.get(productId)
    product.reviews.sort().each {it ->
      ReviewWrapper wrapper = new ReviewWrapper(it)
      reviews.add(wrapper)
    }
  }

  @Command
  public void zoomImage() {

    ProductEntity productEntity = ProductEntity.get(productId)
    String path = new PathBuilder()
        .appendPath(serverFoldersService.productImages)
        .appendString(productEntity.engImagePath)
        .build()
    String std_name = STD_FILE_NAMES.PRODUCT_NAME.getName()
    int std_size = STD_IMAGE_SIZES.LARGE.getSize()

    AImage aImage = imageService.getImageFile(path, std_name, std_size)

    Map<Object, Object> params = new HashMap<Object, Object>()
    params.put("image", aImage)

    Window wnd = Executions.createComponents("/zul/shop/windows/zoomImageWnd.zul", null, params) as Window
    wnd.setWidth("520px")
    wnd.setHeight("560px")
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    wnd.doModal()
    wnd.setVisible(true)
  }

  @Command
  public void addReviews() {
    Map<Object, Object> params = new HashMap<Object, Object>()
    params.put("id", productId)

    Window wnd = Executions.createComponents("/zul/shop/reviewsWnd.zul", null, params) as Window
    wnd.setPage(ExecutionsCtrl.getCurrentCtrl().getCurrentPage())
    wnd.doModal()
    wnd.setVisible(true)
  }

  @Command
  public void addToCart() {
    model.setInCart(true)
    cartService.addToCart(ProductEntity.get(productId))
    BindUtils.postNotifyChange(null, null, model, "inCart");
  }

  @Override
  void setGrailsApplication(GrailsApplication grailsApplication) {
    this.grailsApplication = grailsApplication
  }

}
