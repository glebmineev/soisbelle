package ru.spb.soisbelle

class EmailTemplateEntity {

  String name
  String emailHtml

  static mapping = {
    table: 'emailtemplate'
    columns {
      id column: 'emailtemplate_id'
      name column: 'emailconfig_name'
      emailHtml column: 'emailtemplate_emailhtml'
    }

    version false
  }

  static constraints = {
    name nullable: true, maxSize: 65535
    emailHtml nullable: true, maxSize: 65535
  }

}
