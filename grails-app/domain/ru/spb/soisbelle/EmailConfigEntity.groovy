package ru.spb.soisbelle

class EmailConfigEntity {

  String name

  String email
  String host
  String port
  String auth
  String starttls
  String login
  String password

  static mapping = {
    table: 'emailconfig'
    columns {
      id column: 'emailconfig_id'
      name column: 'emailconfig_name'
      email column: 'emailconfig_email'
      host column: 'emailconfig_host'
      port column: 'emailconfig_port'
      auth column: 'emailconfig_auth'
      starttls column: 'emailconfig_starttls'
      login column: 'emailconfig_login'
      password column: 'emailconfig_password'
    }

    version false
  }

  static constraints = {
    name nullable: true, maxSize: 65535
    email nullable: true, maxSize: 65535
    host nullable: true, maxSize: 65535
    port nullable: true, maxSize: 65535
    auth nullable: true, maxSize: 65535
    starttls nullable: true, maxSize: 65535
    login nullable: true, maxSize: 65535
    password nullable: true, maxSize: 65535
  }

}
