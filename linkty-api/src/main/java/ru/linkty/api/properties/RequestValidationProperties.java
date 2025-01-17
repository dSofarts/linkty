package ru.linkty.api.properties;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.linkty.api.constant.Constants;
import ru.linkty.api.exception.ValidationException;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "request-validation")
public class RequestValidationProperties {

  private List<Consumer> consumers;

  public void validateRequest(Map<String, String> headers, boolean needsAuth) {
    validateRequestHeaders(headers, needsAuth);
  }

  private void validateRequestHeaders(Map<String, String> headers, boolean needsAuth) {
    if (!headers.containsKey(Constants.SERVICE_NAME)) {
      throw new ValidationException(String.format("Required header not specified %s",
          Constants.SERVICE_NAME), HttpStatus.UNAUTHORIZED);
    }
    Consumer activeConsumer = findActiveConsumer(headers.get(Constants.SERVICE_NAME));
    if (activeConsumer == null) {
      throw new ValidationException(
          String.format("The service-name header %s value is not in the allowed list",
              headers.get(Constants.SERVICE_NAME)), HttpStatus.FORBIDDEN);
    }

    if (needsAuth) {
      if (!headers.containsKey(Constants.USER_ID_HEADER)) {
        throw new ValidationException(String.format("Required header not specified %s",
            Constants.USER_ID_HEADER), HttpStatus.UNAUTHORIZED);
      }
      try {
        UUID.fromString(headers.get(Constants.USER_ID_HEADER));
      } catch (Exception e) {
        throw new ValidationException("User id is not valid", HttpStatus.FORBIDDEN);
      }
    }
  }

  private Consumer findActiveConsumer(String serviceName) {
    return consumers.stream()
        .filter(consumer -> consumer.serviceName.equals(serviceName))
        .findFirst()
        .orElse(null);
  }

  @Data
  private static class Consumer {

    private String serviceName;
    private boolean alwaysAllowed;
  }
}
