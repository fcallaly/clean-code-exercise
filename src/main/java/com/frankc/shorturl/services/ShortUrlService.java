package com.frankc.shorturl.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;

import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
/**
 * Interface to ShortUrl Service layer.
 */
import com.frankc.shorturl.entities.ShortUrl;

public interface ShortUrlService {

    List<ShortUrl> findAll(Pageable pageRequest);
    ShortUrl find(String shortUrlPath) throws NoSuchElementException;
    ShortUrl createShortUrl(String redirectTo) throws IllegalArgumentException, MaxPathGenerationRetriesException;
    void deleteByShortUrlPath(String shortUrlPath)throws NoSuchElementException;
}
