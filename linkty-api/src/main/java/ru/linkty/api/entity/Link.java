package ru.linkty.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link")
public class Link {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  private String link;
  private String shortLink;
  @Column(nullable = false)
  @Builder.Default
  private ZonedDateTime expired = ZonedDateTime.now().plusDays(7);
  @Column(nullable = false)
  @Builder.Default
  private ZonedDateTime created = ZonedDateTime.now();
  private Integer limitRedirect;
  @Column(nullable = false)
  @Builder.Default
  private Integer usages = 0;
  @Column(nullable = false)
  @Builder.Default
  private boolean isValid = true;
}
