package ru.linkty.api.utils;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.linkty.api.dto.response.FrontResponse;
import ru.linkty.api.exception.ValidationException;

@Slf4j
public class RestUtils {

  private RestUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static ResponseEntity<FrontResponse> failReturn(String message, HttpStatus status,
      String entityId) {
    FrontResponse.FrontResponseBuilder responseDto = FrontResponse.builder()
        .status(message)
        .statusCode(String.valueOf(status.value()));
    return ResponseEntity.status(status).body(Objects.isNull(entityId) ?
        responseDto.build() : responseDto.objectId(entityId).build());
  }

  public static ResponseEntity<FrontResponse> failReturn(String message, HttpStatus status) {
    return failReturn(message, status, null);
  }

  public static HttpStatus findHttpStatus(Object response) {
    if (response instanceof FrontResponse) {
      return HttpStatus.valueOf(Integer.parseInt(((FrontResponse) response).getStatusCode()));
    }
    return HttpStatus.OK;
  }

  public static void serviceNameCheck(String serviceName) {
    log.error("Not permission for this service-name: {} to execute request", serviceName);
    throw new ValidationException(
        String.format("Not permission for this service-name: %s to execute request",
            serviceName), HttpStatus.FORBIDDEN);
  }
}
