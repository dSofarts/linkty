package ru.linkty.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.linkty.api.annotation.LoggingUsed;
import ru.linkty.api.controller.api.RedirectControllerApi;
import ru.linkty.api.dto.response.RedirectResponse;
import ru.linkty.api.service.LinkService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class RedirectController implements RedirectControllerApi {

  private final LinkService linkService;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "/c/{}")
  public ResponseEntity<RedirectResponse> getRedirectLink(String shortLink) {
    return ResponseEntity.ok(linkService.getRedirectLink(shortLink));
  }
}
