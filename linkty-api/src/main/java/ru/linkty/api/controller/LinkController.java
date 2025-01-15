package ru.linkty.api.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.linkty.api.annotation.LoggingUsed;
import ru.linkty.api.constant.Constants;
import ru.linkty.api.controller.api.LinkControllerApi;
import ru.linkty.api.controller.validator.MainValidator;
import ru.linkty.api.dto.request.CreateLinkRequest;
import ru.linkty.api.dto.request.DeleteLinkRequest;
import ru.linkty.api.dto.request.UpdateLinkRequest;
import ru.linkty.api.dto.response.FrontResponse;
import ru.linkty.api.dto.response.LinkResponse;
import ru.linkty.api.dto.response.LinksResponse;
import ru.linkty.api.service.LinkService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class LinkController implements LinkControllerApi {

  private final LinkService linkService;
  private final MainValidator mainValidator;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "/links/create-link")
  public ResponseEntity<LinkResponse> createLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody CreateLinkRequest requestBody) {
    mainValidator.validateRequest(requestHeaders);
    return ResponseEntity.ok(linkService.createLink(requestHeaders
        .get(Constants.USER_ID_HEADER), requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "/links/update-link")
  public ResponseEntity<LinkResponse> updateLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody UpdateLinkRequest requestBody) {
    mainValidator.validateRequest(requestHeaders);
    return ResponseEntity.ok(linkService.updateLink(requestHeaders
        .get(Constants.USER_ID_HEADER), requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "/links/delete-link")
  public ResponseEntity<FrontResponse> deleteLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody DeleteLinkRequest requestBody) {
    mainValidator.validateRequest(requestHeaders);
    return ResponseEntity.ok(new FrontResponse());
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "/links")
  public ResponseEntity<LinksResponse> getLinks(
      @RequestHeader Map<String, String> requestHeaders) {
    mainValidator.validateRequest(requestHeaders);
    return ResponseEntity.ok(linkService.getLinks(requestHeaders
        .get(Constants.USER_ID_HEADER)));
  }
}
