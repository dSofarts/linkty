package ru.linkty.api.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import ru.linkty.api.annotation.ObjectId;
import ru.linkty.api.dto.request.CreateLinkRequest;
import ru.linkty.api.dto.request.UpdateLinkRequest;
import ru.linkty.api.dto.response.FrontResponse;
import ru.linkty.api.dto.response.LinksResponse;
import ru.linkty.api.dto.response.RedirectResponse;

@Tag(name = "Api для создания коротких ссылок")
@RequestMapping(value = "/links", produces = MediaType.APPLICATION_JSON_VALUE)
public interface LinkControllerApi {

  @PostMapping("/create-link")
  @Operation(summary = "Запрос на создание короткой ссылки",
      description = "Запрос на создание короткой ссылки")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = LinksResponse.class)),
          description = "Короткая ссылка успешно создана"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка во входящих данных"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен без авторизации"
      ),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка сервера")
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String")),
      @Parameter(
          name = "x-user-id",
          description = "ID пользователя",
          example = "4c6fafe5-6fa0-491c-8fc9-b3a0c9c768a7",
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<LinksResponse> createLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody CreateLinkRequest requestBody);

  @PatchMapping("/update-link")
  @Operation(summary = "Запрос на обновление короткой ссылки",
      description = "Запрос на обновление короткой ссылки")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = LinksResponse.class)),
          description = "Короткая ссылка успешно обновлена"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка во входящих данных"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен без авторизации"
      ),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка сервера")
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String")),
      @Parameter(
          name = "x-user-id",
          description = "ID пользователя",
          example = "4c6fafe5-6fa0-491c-8fc9-b3a0c9c768a7",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<LinksResponse> updateLink(
      @RequestHeader Map<String, String> requestHeaders,
      @RequestBody UpdateLinkRequest requestBody);

  @DeleteMapping("/delete-link/{linkId}")
  @Operation(summary = "Запрос на удаление короткой ссылки",
      description = "Запрос на удаление короткой ссылки")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Короткая ссылка успешно удалена"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка во входящих данных"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен без авторизации"
      ),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка сервера")
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String")),
      @Parameter(
          name = "x-user-id",
          description = "ID пользователя",
          example = "4c6fafe5-6fa0-491c-8fc9-b3a0c9c768a7",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String")),
      @Parameter(
          name = "linkId",
          description = "Идентификатор короткой ссылки",
          example = "4c6fafe5-6fa0-491c-8fc9-b3a0c9c768a7",
          required = true,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<FrontResponse> deleteLink(
      @RequestHeader Map<String, String> requestHeaders,
      @PathVariable("linkId") @ObjectId String linkId);

  @GetMapping("/get/{userId}")
  @Operation(summary = "Запрос на получение всех коротких ссылок пользователя",
      description = "Запрос на получение всех коротких ссылок пользователя")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = LinksResponse.class)),
          description = "Успешно обработан запрос"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка во входящих данных"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен без авторизации"
      ),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка сервера")
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String")),
      @Parameter(
          name = "userId",
          description = "Идентификатор пользователя",
          example = "4c6fafe5-6fa0-491c-8fc9-b3a0c9c768a7",
          required = true,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<LinksResponse> getLinks(
      @RequestHeader Map<String, String> requestHeaders,
      @PathVariable("userId") @ObjectId String userId);

  @GetMapping("/redirect/{shortLink}")
  @Operation(summary = "Запрос на получение основной ссылки",
      description = "Запрос на получение основной ссылки")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = LinksResponse.class)),
          description = "Ссылка успешно получена"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка во входящих данных"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен без авторизации"
      ),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponse.class)),
          description = "Ошибка сервера")
  })
  @Parameters({
      @Parameter(
          name = "shortLink",
          description = "Короткая ссылка",
          example = "1a3B5c",
          required = true,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<RedirectResponse> getRedirectLink(
      @RequestHeader Map<String, String> requestHeaders,
      @PathVariable("shortLink") @ObjectId String shortLink);
}
