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
import ru.linkty.api.dto.response.FrontResponse;
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
  public LinksResponse createLink(@Nullable String userId, CreateLinkRequest linkRequest) {
    log.info("Start created link process");
    User user = null;
    if (Objects.nonNull(userId)) {
      log.info("User founded: {}", userId);
      user = userRepository.findUserById(UUID.fromString(userId)).orElse(null);
    }
    if (user == null) {
      log.info("User does not exist");
      user = createUser();
    }

    Link link = user.getLinks().stream().filter(s -> s.getLink().equals(linkRequest.getLink()))
        .findFirst().orElse(null);
    if (link == null) {
      log.info("The user does not have an equivalent link");
      generateLink(user, linkRequest);
    }
    return getLinks(user.getId().toString());
  }

  private void generateLink(User user, CreateLinkRequest linkRequest) {
    log.info("Start generating link process");
    ZonedDateTime expired =
        ZonedDateTime.now().plusDays(expiredDays).isAfter(linkRequest.getExpired())
            ? linkRequest.getExpired() : ZonedDateTime.now().plusDays(expiredDays);

    Link link = linkRepository.save(Link.builder()
        .limitRedirect(linkRequest.getLimit())
        .link(linkRequest.getLink())
        .expired(expired)
        .user(user)
        .shortLink(generateShortLink()).build());
    user.getLinks().add(link);
    log.info("Link successfully generated with id: {}", link.getId());
  }

  private User createUser() {
    log.info("Start created user process");
    User user = userRepository.save(User.builder()
        .name("User")
        .links(new ArrayList<>()).build());
    log.info("User successful created with id: {}", user.getId().toString());
    return user;
  }

  private String generateShortLink() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder(6);

    for (int i = 0; i < 6; i++) {
      int randomIndex = random.nextInt(chars.length());
      sb.append(chars.charAt(randomIndex));
    }

    log.info("Generated short link: {}", sb);
    return sb.toString();
  }

  @Override
  @Transactional
  public LinksResponse updateLink(String userId, UpdateLinkRequest linkRequest) {
    log.info("Start updating link process");
    User user = userRepository.findUserById(UUID.fromString(userId)).orElseThrow(
        () -> new NotFoundException(userId, "User does not exist"));
    Link link = user.getLinks().stream().filter(l -> l.getId().toString()
        .equals(linkRequest.getLinkId())).findFirst().orElseThrow(
        () -> new NotFoundException(linkRequest.getLinkId(), "User does not have this link"));
    log.info("Link successfully founded with id: {}", link.getId());

    if (linkRequest.getLinkInfo().getLimit() != null) {
      link.setLimitRedirect(linkRequest.getLinkInfo().getLimit());
      log.info("From the link: {} has successfully updated its limit to {}",
          link.getId().toString(), linkRequest.getLinkInfo().getLimit());
    }
    if (linkRequest.getLinkInfo().getExpired() != null) {
      link.setExpired(linkRequest.getLinkInfo().getExpired());
      log.info("From the link: {} has successfully updated its expired to {}",
          link.getId().toString(), linkRequest.getLinkInfo().getExpired());
    }
    return getLinks(user.getId().toString());
  }

  @Override
  @Transactional
  public RedirectResponse getRedirectLink(String shortLink) throws NotFoundException,
      ValidationException {
    log.info("Start getting redirect link process");
    Link link = linkRepository.findByShortLink(shortLink).orElseThrow(
        () -> new NotFoundException(shortLink, "Not found shortLink"));
    if (link.isValid()
        && link.getExpired().isAfter(ZonedDateTime.now())
        && (link.getLimitRedirect() == null || link.getUsages() < link.getLimitRedirect())) {
      link.setUsages(link.getUsages() + 1);
      log.info("Link: {} is valid", link.getId().toString());
      if (link.getLimitRedirect() != null && link.getUsages() >= link.getLimitRedirect()) {
        log.info("Link: {} used for the last time", link.getId().toString());
        link.setValid(false);
      }
      return RedirectResponse.builder()
          .url(link.getLink()).build();
    }
    log.warn("Link: {} is not valid", link.getId().toString());
    throw new ValidationException("Link is not valid", HttpStatus.BAD_REQUEST);
  }

  @Override
  public LinksResponse getLinks(String userId) throws NotFoundException {
    log.info("Start getting links process");
    if (userId == null) {
      log.warn("UserId is null");
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

  @Override
  @Transactional
  public FrontResponse deleteLink(String userId, String linkId) {
    log.info("Start deleting link process");
    User user = userRepository.findUserById(UUID.fromString(userId)).orElseThrow(
        () -> new NotFoundException(userId, "Not found user"));
    Link link = user.getLinks().stream().filter(l -> l.getId().toString().equals(linkId))
        .findFirst().orElseThrow(
            () -> new NotFoundException(linkId, "User does not have this link"));
    linkRepository.delete(link);
    user.getLinks().remove(link);
    log.info("Successfully deleted link: {}", link.getId().toString());
    return FrontResponse.builder()
        .objectId(link.getId().toString())
        .statusCode("200")
        .status("Successfully deleted link")
        .build();
  }
}
