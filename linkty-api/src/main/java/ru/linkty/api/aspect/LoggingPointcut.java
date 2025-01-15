package ru.linkty.api.aspect;

import org.aspectj.lang.annotation.Pointcut;
import ru.linkty.api.annotation.LoggingUsed;

public class LoggingPointcut {
  @Pointcut("@annotation(loggingUsed)")
  public void logController(LoggingUsed loggingUsed) {

  }
}
