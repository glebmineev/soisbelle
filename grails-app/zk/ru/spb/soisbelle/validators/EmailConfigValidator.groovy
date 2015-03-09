package ru.spb.soisbelle.validators

import com.google.common.base.Strings
import org.zkoss.bind.ValidationContext
import org.zkoss.bind.validator.AbstractValidator
import ru.spb.soisbelle.UserEntity

/**
 * Created with IntelliJ IDEA.
 * User: gleb
 * Date: 5/8/13
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
class EmailConfigValidator extends AbstractValidator {

  @Override
  void validate(ValidationContext ctx) {
    validateStringValue(ctx, ctx.getValidatorArg("host") as String, "host")
    validateStringValue(ctx, ctx.getValidatorArg("port") as String, "port")
    validateStringValue(ctx, ctx.getValidatorArg("auth") as String, "auth")
    validateStringValue(ctx, ctx.getValidatorArg("starttls") as String, "starttls")
    validateStringValue(ctx, ctx.getValidatorArg("login") as String, "login")
    validateStringValue(ctx, ctx.getValidatorArg("password") as String, "password")
  }

  public void validateStringValue(ValidationContext ctx, String value, String fieldName){
    if (Strings.isNullOrEmpty(value))
      this.addInvalidMessage(ctx, fieldName, "Поле не может быть пустым");
  }

}
