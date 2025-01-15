package ru.linkty.api.utils;

import static ru.linkty.api.constant.Constants.SERVICE_NAME;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtils {

  private ValidationUtils() {
    throw new IllegalStateException("Utility class");
  }


  public static boolean isValidRequestHeader(Map<String, String> requestHeaders) {
    return requestHeaders.containsKey(SERVICE_NAME)
        && !requestHeaders.get(SERVICE_NAME).isEmpty();
  }
}
