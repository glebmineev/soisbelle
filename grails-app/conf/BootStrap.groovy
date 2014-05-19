import org.grails.datastore.mapping.transactions.TransactionUtils
import ru.spb.soisbelle.*
import ru.spb.soisbelle.importer.ConverterRU_EN

class BootStrap {

  def initService
  def imageService

  def init = { servletContext ->

    ProductPropertyEntity.withTransaction {
      ProductPropertyEntity.findOrSaveWhere(name: "Объем")
    }

    FilterGroupEntity.withTransaction {
      //FilterGroupEntity.findOrSaveWhere(name: "Производитель")
      FilterGroupEntity.findOrSaveWhere(name: "Применение")
    }

    CategoryEntity.withTransaction {
      CategoryEntity.findOrSaveWhere(name: "Для волос", translitName: ConverterRU_EN.translit("Для волос"))
      CategoryEntity.findOrSaveWhere(name: "Для тела", translitName: ConverterRU_EN.translit("Для тела"))
      CategoryEntity.findOrSaveWhere(name: "Для рук", translitName: ConverterRU_EN.translit("Для рук"))
      CategoryEntity.findOrSaveWhere(name: "Депиляция", translitName: ConverterRU_EN.translit("Депиляция"))
      CategoryEntity.findOrSaveWhere(name: "Для мужчин", translitName: ConverterRU_EN.translit("Для мужчин"))
      CategoryEntity.findOrSaveWhere(name: "Аксессуары", translitName: ConverterRU_EN.translit("Аксессуары"))
    }

    ManufacturerEntity.withTransaction {
      ManufacturerEntity.findOrSaveWhere(name: "Indola", shortName: 'Indola')
      ManufacturerEntity.findOrSaveWhere(name: "Periche", shortName: 'Periche')
      ManufacturerEntity.findOrSaveWhere(name: "Hair light", shortName: 'Hair')
      ManufacturerEntity.findOrSaveWhere(name: "OLLIN Professional", shortName: 'OLLIN')
      ManufacturerEntity.findOrSaveWhere(name: "Schwarzkopf professional", shortName: 'Schwarzkopf')
      ManufacturerEntity.findOrSaveWhere(name: "Screen", shortName: 'Screen')
    }

    RoleEntity.withTransaction {
      RoleEntity.findOrSaveWhere(name: "MANAGER")
      RoleEntity.findOrSaveWhere(name: "USER")
    }

    UserEntity.withTransaction {
      UserEntity user = UserEntity.findOrSaveWhere(
          login: "admin",
          password: "admin".encodeAsSHA1(),
          email: "admin@admin.ru",
          fio: "fio",
          address: "SPb",
          isActive: true
      )

      user.addToGroups(RoleEntity.findWhere(name: "MANAGER"))
      user.save()

    }

    InfoEntity.withTransaction {status ->
      List<InfoEntity> list = InfoEntity.list()
      if (list == null || list.size() == 0){
        InfoEntity infoEntity = new InfoEntity()
        infoEntity.save(flush: true)
      }

    }

    OrderIdentEntity.withTransaction {
      OrderIdentEntity.findOrSaveWhere(ident: 100000000L)
    }

    AdminMenuItemEntity.withTransaction {

      AdminMenuItemEntity editors = AdminMenuItemEntity.findOrSaveWhere(name: "Редакторы", href: null, parentMenuItem: null)
      AdminMenuItemEntity.findOrSaveWhere(name: "Заказы", href: "admin/orders", parentMenuItem: null)
      AdminMenuItemEntity.findOrSaveWhere(name: "Перейти в магазин", href: "shop/index", parentMenuItem: null)
      AdminMenuItemEntity.findOrSaveWhere(name: "Импорт", href: "admin/importCatalog", parentMenuItem: null)

      AdminMenuItemEntity.findOrSaveWhere(name: "Категории", href: "admin/categories", parentMenuItem: editors)
      AdminMenuItemEntity.findOrSaveWhere(name: "Информация", href: "admin/info", parentMenuItem: editors)
      AdminMenuItemEntity.findOrSaveWhere(name: "Производители", href: "admin/manufacturers", parentMenuItem: editors)
      AdminMenuItemEntity.findOrSaveWhere(name: "Фильтры", href: "admin/filters", parentMenuItem: editors)
      AdminMenuItemEntity.findOrSaveWhere(name: "Редактор", href: "admin/editor", parentMenuItem: editors)
      AdminMenuItemEntity.findOrSaveWhere(name: "Пользователи", href: "admin/users", parentMenuItem: editors)
      AdminMenuItemEntity.findOrSaveWhere(name: "Товары", href: "admin/products" , parentMenuItem: editors)

    }

    //imageService.syncAllImagesWithServer()

  }

  def destroy = {
  }
}
