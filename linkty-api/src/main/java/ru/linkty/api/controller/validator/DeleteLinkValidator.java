package ru.linkty.api.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.linkty.api.dto.request.DeleteLinkRequest;

public class DeleteLinkValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return DeleteLinkRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    DeleteLinkRequest request = (DeleteLinkRequest) target;
    ValidationUtils.rejectIfEmpty(errors, "linkId", "linkId");
    if (request.getLinkId() != null && request.getLinkId().length() != 36) {
      errors.reject("linkId", "linkId");
    }
  }
}
