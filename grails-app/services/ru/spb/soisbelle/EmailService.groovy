package ru.spb.soisbelle

import groovy.text.GStringTemplateEngine
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

import javax.mail.*
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailService implements InitializingBean {

  static transactional = true
  static scope = "session";
  static proxy = true

  GrailsApplication grailsApplication =
    ApplicationHolder.getApplication().getMainContext().getBean("grailsApplication") as GrailsApplication

  //Логгер
  static Logger logger = LoggerFactory.getLogger(EmailService.class)

  String HOST //= "smtp.gmail.com"
  String PORT //= "587"
  String AUTH //= "true"
  String STARTTLS //= "true"
  String LOGIN //= "soisbelletest@gmail.com"
  String PASSWORD //= "QWglebmineev1987"

  String passwordRecoveryTemplate
  String newOrderTemplate
  String registrationTemplate

  @Override
  void afterPropertiesSet() throws Exception {
    initEmailConfig()
    initTemplates()
  }

  void initEmailConfig() {
    EmailConfigEntity emailConfig = EmailConfigEntity.findByName("Конфигурация")
    HOST = emailConfig.getHost()
    PORT = emailConfig.getPort()
    AUTH = emailConfig.getAuth()
    STARTTLS = emailConfig.getStarttls()
    LOGIN = emailConfig.getLogin()
    PASSWORD = emailConfig.getPassword()
  }

  void initTemplates() {
    passwordRecoveryTemplate = EmailTemplateEntity.findByName("Восстановление пароля")?.getEmailHtml()
    newOrderTemplate = EmailTemplateEntity.findByName("Заказ")?.getEmailHtml()
    registrationTemplate = EmailTemplateEntity.findByName("Регистрация")?.getEmailHtml()
  }

  /**
   * Отсыл почты с хэшем для регистрации.
   * @param to - кому шлем.
   * @param hash - хеш.
   */
  void sendConfirmationEmail(String login, String to, String hash) {

    try {

      Session session = getSession()

      MimeMessage message = getMessage(session, "Подтверждение регистрации", to)

      Multipart mp = new MimeMultipart();

      def binding = ["login": login, "email": to, "activation_code": hash]
      //File file = grailsApplication.mainContext.getResource("email/registration_request.template").getFile()
      GStringTemplateEngine engine = new GStringTemplateEngine();
      Writable data = engine.createTemplate(registrationTemplate).make(binding)

      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(data.toString(), "text/html; charset=utf-8");
      mp.addBodyPart(htmlPart);
      message.setContent(mp)

      Transport.send(message);

    } catch (MessagingException e) {
      logger.error(e.getMessage())
    }
  }

  /**
   * Сброс пароля пользователя.
   * @param to - почта пользователя, куда слать новый пароль
   */
  void sendPassEmail(String to, String new_pass){
    Session session = getSession()
    MimeMessage message = getMessage(session, "Восстановление пароля", to)

    Multipart mp = new MimeMultipart();

    /*String htmlBody =
      new Scanner(grailsApplication.mainContext.getResource("email/newPass_template.html").file, "UTF-8").useDelimiter("\\A").next();

    String replaced = htmlBody.replace("NEW_PASS", new_pass)*/

    def binding = ["new_pass": new_pass]
    //File file = grailsApplication.mainContext.getResource("email/new_order.template").getFile()
    GStringTemplateEngine engine = new GStringTemplateEngine();
    Writable data = engine.createTemplate(passwordRecoveryTemplate).make(binding)

    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(data.toString(), "text/html; charset=utf-8");
    mp.addBodyPart(htmlPart);
    message.setContent(mp)

    Transport.send(message);
  }


  /**
   * Отсыл пистма с номером заказа пользователя.
   * @param to - почта пользователя, куда информацию о заказе
   */
  void sendUserOrderEmail(String orderNumber, String fio, String to){
    Session session = getSession()
    MimeMessage message = getMessage(session, "Номер заказа в интернет магазине \"Sois belle\"", to)

    Multipart mp = new MimeMultipart();

    def binding = ["order_number": orderNumber, "user_name": fio]
    //File file = grailsApplication.mainContext.getResource("email/new_order.template").getFile()
    GStringTemplateEngine engine = new GStringTemplateEngine();
    Writable data = engine.createTemplate(newOrderTemplate).make(binding)

    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(data.toString(), "text/html; charset=utf-8");
    mp.addBodyPart(htmlPart);
    message.setContent(mp)

    Transport.send(message);
  }

  /**
   * Создание сессии для отправки эл. почты.
   * @return сессия.
   */
  private Session getSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", HOST);
    props.put("mail.smtp.auth", AUTH);
    props.put("mail.smtp.port", PORT);
    props.put("mail.smtp.starttls.enable", STARTTLS);

    return Session.getInstance(props,
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(LOGIN, PASSWORD);
          }
        });
  }

  /**
   * Формирвание сообщения по email.
   * @param session - сессия
   * @param to - кому слать(эл. ящик)
   * @return сообщение.
   */
  private MimeMessage getMessage(Session session, String subj, String to) {
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress("glebsis"));
    message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(to));
    message.setSubject(subj, "UTF-8");
    message
  }


}
