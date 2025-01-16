package ru.linkty.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.linkty.api.utils.time.DateToStringConverter;
import ru.linkty.api.utils.time.TimeDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateLinkRequest {

  private String link;
  private Integer limit;
  @JsonDeserialize(using = TimeDeserializer.class)
  @JsonSerialize(converter = DateToStringConverter.class)
  private ZonedDateTime expired;
}
