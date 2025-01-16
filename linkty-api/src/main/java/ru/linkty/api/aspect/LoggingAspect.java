package ru.linkty.api.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.linkty.api.annotation.LoggingUsed;
import ru.linkty.api.annotation.ShortLink;
import ru.linkty.api.constant.Constants;
import ru.linkty.api.exception.NotFoundException;
import ru.linkty.api.exception.ValidationException;
import ru.linkty.api.utils.RestUtils;
import ru.linkty.api.utils.ValidationUtils;
import ru.linkty.utils.masking.service.MaskingService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

  private final MaskingService maskingService;
  private static final String REQUEST_HEADER_EXCEPTION = "Not found required header in request";

  @SneakyThrows
  @Around(value = "ru.linkty.api.aspect.LoggingPointcut.logController(loggingUsed)",
      argNames = "joinPoint,loggingUsed")
  public Object logEvent(ProceedingJoinPoint joinPoint, LoggingUsed loggingUsed) {
    String operationId = UUID.randomUUID().toString();
    log.debug("Start logging process via aspect: {}", operationId);
    Object[] args = joinPoint.getArgs();
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

    Map<String, String> requestHeaders = new HashMap<>();
    Map<String, String> requestParams = new HashMap<>();
    Object requestBody = null;
    String shortLink = null;

    for (int i = 0; i < method.getParameterAnnotations().length; i++) {
      Annotation[] annotations = method.getParameterAnnotations()[i];
      for (Annotation annotation : annotations) {
        if (annotation.annotationType().equals(RequestHeader.class)) {
          requestHeaders = (Map<String, String>) args[i];
        }
        if (annotation.annotationType().equals(RequestParam.class)) {
          requestParams = (Map<String, String>) args[i];
        }
        if (annotation.annotationType().equals(RequestBody.class)) {
          requestBody = args[i];
        }
        if (annotation.annotationType().equals(ShortLink.class)) {
          shortLink = (String) args[i];
        }
      }
    }

    String endpoint = shortLink == null ? loggingUsed.endpoint() : loggingUsed.endpoint()
        .replace("{}", shortLink);
    log.info("endpoint: {}, operationId: {}, objectId: {}, service-name: {}, requestBody: {},"
            + " requestParams: {}",
        endpoint, operationId, shortLink, requestHeaders.get(Constants.SERVICE_NAME),
        maskingService.writeAsString(requestBody), requestParams);

    ResponseEntity<?> response;
    try {
      if (!endpoint.contains("/redirect/")) {
        validationRequestHeaders(requestHeaders);
      }
      response = (ResponseEntity) joinPoint.proceed(args);
    } catch (ValidationException e) {
      log.error("Error: {}", e.getMessage());
      return RestUtils.failReturn(e.getMessage(), e.getStatus());
    } catch (JsonProcessingException e) {
      log.error("Error: {}", e.getMessage());
      return RestUtils.failReturn(e.getMessage(), HttpStatus.BAD_REQUEST, shortLink);
    } catch (NotFoundException e) {
      log.error("Error: {}", e.getMessage());
      return RestUtils.failReturn(e.getMessage(), HttpStatus.NOT_FOUND, shortLink);
    } catch (Exception e) {
      log.error("Internal exception: {}", e.getMessage(), e);
      return RestUtils.failReturn(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, shortLink);
    }

    Object responseBody = response.getBody();
    log.info("Response data: {}", maskingService.writeAsString(responseBody));
    log.debug("Finish logging process via aspect: {}", operationId);
    return response;
  }

  private void validationRequestHeaders(Map<String, String> requestHeaders) {
    if (!ValidationUtils.isValidRequestHeader(requestHeaders)) {
      throw new ValidationException(REQUEST_HEADER_EXCEPTION, HttpStatus.UNAUTHORIZED);
    }
  }
}
