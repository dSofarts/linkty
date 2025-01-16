package ru.linkty.api.service.impl;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.linkty.api.dto.request.CreateLinkRequest;
import ru.linkty.api.dto.request.UpdateLinkRequest;
import ru.linkty.api.dto.response.LinkResponse;
import ru.linkty.api.dto.response.LinksResponse;
import ru.linkty.api.dto.response.RedirectResponse;
import ru.linkty.api.entity.Link;
import ru.linkty.api.entity.User;
import ru.linkty.api.exception.NotFoundException;
import ru.linkty.api.exception.ValidationException;
import ru.linkty.api.repository.LinkRepository;
import ru.linkty.api.repository.UserRepository;
import ru.linkty.api.service.LinkService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

  private final LinkRepository linkRepository;
  private final UserRepository userRepository;
  @Value("${link.expired:1}")
  private Integer expiredDays;

  @Override
  public LinkResponse createLink(@Nullable String userId, CreateLinkRequest linkRequest) {
    User user = null;
    if (Objects.nonNull(userId)) {
      user = userRepository.findUserById(UUID.fromString(userId)).orElse(null);
    }
    if (user == null) {
      user = createUser();
    }

    Link link = user.getLinks().stream().filter(s -> s.getLink().equals(linkRequest.getLink()))
        .findFirst().orElse(null);
    return link == null ? generateLink(user, linkRequest) : LinkResponse.builder()
        .userId(user.getId().toString())
        .shortLink(link.getShortLink()).build();
  }

  private LinkResponse generateLink(User user, CreateLinkRequest linkRequest) {
    ZonedDateTime expired =
        ZonedDateTime.now().plusDays(expiredDays).isAfter(linkRequest.getExpired())
            ? linkRequest.getExpired() : ZonedDateTime.now().plusDays(expiredDays);

    Link l = linkRepository.save(Link.builder()
        .limitRedirect(linkRequest.getLimit())
        .link(linkRequest.getLink())
        .expired(expired)
        .user(user)
        .shortLink(generateShortLink()).build());
    return LinkResponse.builder()
        .expired(expired)
        .shortLink(l.getShortLink())
        .userId(user.getId().toString()).build();
  }

  private User createUser() {
    User user = User.builder()
        .name("Пользователь")
        .links(new ArrayList<>()).build();
    return userRepository.save(user);
  }

  private String generateShortLink() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder(6);

    for (int i = 0; i < 6; i++) {
      int randomIndex = random.nextInt(chars.length());
      sb.append(chars.charAt(randomIndex));
    }

    return sb.toString();
  }

  @Override
  public LinkResponse updateLink(String userId, UpdateLinkRequest linkRequest) {
    return null;
  }

  @Override
  @Transactional
  public RedirectResponse getRedirectLink(String shortLink) throws NotFoundException,
      ValidationException {
    Link link = linkRepository.findByShortLink(shortLink).orElseThrow(
        () -> new NotFoundException(shortLink, "Not found shortLink"));
    if (link.isValid()
        && link.getExpired().isAfter(ZonedDateTime.now())
        && (link.getLimitRedirect() == null || link.getUsages() < link.getLimitRedirect())) {
      link.setUsages(link.getUsages() + 1);
      if (link.getLimitRedirect() != null && link.getUsages() >= link.getLimitRedirect()) {
        link.setValid(false);
      }
      return RedirectResponse.builder()
          .url(link.getLink()).build();
    }
    throw new ValidationException("Link is not valid", HttpStatus.BAD_REQUEST);
  }

  @Override
  public LinksResponse getLinks(String userId) throws NotFoundException {
    if (userId == null) {
      throw new ValidationException("User ID is null", HttpStatus.BAD_REQUEST);
    }
    User user = userRepository.findUserById(UUID.fromString(userId)).orElseThrow(
        () -> new NotFoundException(userId, "Not found user"));
    List<LinksResponse.Link> linkList = new ArrayList<>();
    user.getLinks().forEach(link -> linkList.add(LinksResponse.Link.builder()
        .linkId(link.getId().toString())
        .link(link.getLink())
        .shortLink(link.getShortLink())
        .limit(link.getLimitRedirect())
        .used(link.getUsages())
        .created(link.getCreated())
        .expired(link.getExpired())
        .valid(link.isValid()).build()));
    return LinksResponse.builder()
        .userId(userId)
        .links(linkList).build();
  }
}
