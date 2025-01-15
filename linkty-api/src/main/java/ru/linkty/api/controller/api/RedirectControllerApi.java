package ru.linkty.api.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.linkty.api.annotation.ShortLink;
import ru.linkty.api.dto.response.RedirectResponse;

@Tag(name = "Api для перехода по коротким ссылкам")
@RequestMapping(value = "/c", produces = MediaType.APPLICATION_JSON_VALUE)
public interface RedirectControllerApi {

  @GetMapping("/{shortLink}")
  ResponseEntity<RedirectResponse> getRedirectLink(
      @PathVariable("shortLink") @ShortLink String shortLink);
}
