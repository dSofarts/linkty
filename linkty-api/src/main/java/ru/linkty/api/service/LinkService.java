package ru.linkty.api.service;

import ru.linkty.api.dto.request.CreateLinkRequest;
import ru.linkty.api.dto.request.UpdateLinkRequest;
import ru.linkty.api.dto.response.FrontResponse;
import ru.linkty.api.dto.response.LinksResponse;
import ru.linkty.api.dto.response.RedirectResponse;
import ru.linkty.api.exception.NotFoundException;
import ru.linkty.api.exception.ValidationException;

public interface LinkService {

  LinksResponse createLink(String userId, CreateLinkRequest linkRequest);

  LinksResponse updateLink(String userId, UpdateLinkRequest linkRequest);

  RedirectResponse getRedirectLink(String shortLink) throws NotFoundException,
      ValidationException;

  LinksResponse getLinks(String userId) throws NotFoundException;

  FrontResponse deleteLink(String userId, String linkId);
}
