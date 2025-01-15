package ru.linkty.api.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.linkty.api.entity.Link;

public interface LinkRepository extends JpaRepository<Link, UUID> {

  Optional<Link> findByShortLink(String shortLink);

  void deleteAllByExpiredBefore(ZonedDateTime expiredBefore);
}
