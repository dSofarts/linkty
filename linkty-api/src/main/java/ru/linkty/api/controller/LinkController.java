package ru.linkty.api.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.linkty.api.annotation.LoggingUsed;
import ru.linkty.api.constant.Constants;
import ru.linkty.api.controller.api.LinkControllerApi;
import ru.linkty.api.controller.validator.CreateLinkValidator;
import ru.linkty.api.controller.validator.MainValidator;
import ru.linkty.api.controller.validator.UpdateLinkValidator;
import ru.linkty.api.dto.request.CreateLinkRequest;
import ru.linkty.api.dto.request.UpdateLinkRequest;
import ru.linkty.api.dto.response.FrontResponse;
import ru.linkty.api.dto.response.LinksResponse;
import ru.linkty.api.dto.response.RedirectResponse;
import ru.linkty.api.service.LinkService;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class LinkController implements LinkControllerApi {

  private final LinkService linkService;
  private final MainValidator mainValidator;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /links/create-link")
  public ResponseEntity<LinksResponse> createLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody CreateLinkRequest requestBody) {
    mainValidator.validateRequest(requestBody, CreateLinkValidator.class, requestHeaders);
    return ResponseEntity.ok(linkService.createLink(requestHeaders
        .get(Constants.USER_ID_HEADER), requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "PATCH /links/update-link")
  public ResponseEntity<LinksResponse> updateLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody UpdateLinkRequest requestBody) {
    mainValidator.validateRequest(requestBody, UpdateLinkValidator.class, requestHeaders, true);
    return ResponseEntity.ok(linkService.updateLink(requestHeaders
        .get(Constants.USER_ID_HEADER), requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "DELETE /links/delete-link/{}")
  public ResponseEntity<FrontResponse> deleteLink(
      @RequestHeader Map<String, String> requestHeaders, String linkId) {
    mainValidator.validateRequest(requestHeaders, true);
    return ResponseEntity
        .ok(linkService.deleteLink(requestHeaders.get(Constants.USER_ID_HEADER), linkId));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /links/get/{}")
  public ResponseEntity<LinksResponse> getLinks(
      @RequestHeader Map<String, String> requestHeaders, String userId) {
    mainValidator.validateRequest(requestHeaders);
    return ResponseEntity.ok(linkService.getLinks(userId));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /links/redirect/{}")
  public ResponseEntity<RedirectResponse> getRedirectLink(
      @RequestHeader Map<String, String> requestHeaders, String shortLink) {
    mainValidator.validateRequest(requestHeaders);
    return ResponseEntity.ok(linkService.getRedirectLink(shortLink));
  }
}
