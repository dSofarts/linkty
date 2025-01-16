package ru.linkty.api.controller.validator;

import java.time.ZonedDateTime;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.linkty.api.dto.request.UpdateLinkRequest;

public class UpdateLinkValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return UpdateLinkRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UpdateLinkRequest request = (UpdateLinkRequest) target;
    ValidationUtils.rejectIfEmpty(errors, "linkId", "linkId");
    if (request.getLinkId() != null && request.getLinkId().length() != 36) {
      errors.reject("linkId", "linkId");
    }
    if (request.getLinkInfo() != null && request.getLinkInfo().getLimit() != null
        && request.getLinkInfo().getLimit() < 1) {
      errors.reject("linkInfo.limit", "limit");
    }
    if (request.getLinkInfo() != null && request.getLinkInfo().getExpired() != null
        && request.getLinkInfo().getExpired().isBefore(ZonedDateTime.now())) {
      errors.reject("linkInfo.expired", "expired");
    }
  }
}
