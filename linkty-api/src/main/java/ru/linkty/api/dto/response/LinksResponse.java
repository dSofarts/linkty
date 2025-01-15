package ru.linkty.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import java.util.List;
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
public class LinksResponse {

  private String userId;
  private List<Link> links;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Link {
    private String linkId;
    private String link;
    private String shortLink;
    private Integer limit;
    private int used;
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonSerialize(converter = DateToStringConverter.class)
    private ZonedDateTime created;
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonSerialize(converter = DateToStringConverter.class)
    private ZonedDateTime expired;
    private boolean valid;
  }
}
