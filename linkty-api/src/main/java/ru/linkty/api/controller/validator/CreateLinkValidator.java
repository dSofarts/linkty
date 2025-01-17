package ru.linkty.api.controller.validator;

import java.time.ZonedDateTime;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.linkty.api.dto.request.CreateLinkRequest;

public class CreateLinkValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return CreateLinkRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CreateLinkRequest request = (CreateLinkRequest) target;
    ValidationUtils.rejectIfEmpty(errors, "link", "link");
    ValidationUtils.rejectIfEmpty(errors, "expired", "expired");
    if (request.getExpired() != null && request.getExpired().isBefore(ZonedDateTime.now())) {
      errors.reject("expired", "expired");
    }
  }
}
