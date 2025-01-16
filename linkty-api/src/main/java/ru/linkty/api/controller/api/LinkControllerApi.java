package ru.linkty.api.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.linkty.api.annotation.ShortLink;
import ru.linkty.api.dto.request.CreateLinkRequest;
import ru.linkty.api.dto.request.DeleteLinkRequest;
import ru.linkty.api.dto.request.UpdateLinkRequest;
import ru.linkty.api.dto.response.FrontResponse;
import ru.linkty.api.dto.response.LinkResponse;
import ru.linkty.api.dto.response.LinksResponse;
import ru.linkty.api.dto.response.RedirectResponse;

@Tag(name = "Api для создания коротких ссылок")
@RequestMapping(value = "/links", produces = MediaType.APPLICATION_JSON_VALUE)
public interface LinkControllerApi {

  @PostMapping("/create-link")
  ResponseEntity<LinkResponse> createLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody CreateLinkRequest requestBody);

  @PatchMapping("/update-link")
  ResponseEntity<LinkResponse> updateLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody UpdateLinkRequest requestBody);

  @DeleteMapping("/delete-link")
  ResponseEntity<FrontResponse> deleteLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody DeleteLinkRequest requestBody);

  @GetMapping()
  ResponseEntity<LinksResponse> getLinks(
      @RequestHeader Map<String, String> requestHeaders);

  @GetMapping("/redirect/{shortLink}")
  ResponseEntity<RedirectResponse> getRedirectLink(
      @PathVariable("shortLink") @ShortLink String shortLink);
}
