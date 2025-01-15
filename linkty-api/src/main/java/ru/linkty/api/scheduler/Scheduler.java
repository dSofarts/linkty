package ru.linkty.api.scheduler;

import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.linkty.api.repository.LinkRepository;

@Component
@RequiredArgsConstructor
public class Scheduler {

  private final LinkRepository linkRepository;

  @Transactional
  @Scheduled(fixedRate = 3600000) // Запуск раз в час
  public void cleanupExpiredLinks() {
    linkRepository.deleteAllByExpiredBefore(ZonedDateTime.now());
  }
}
