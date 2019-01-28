package com.frankc.shorturl.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.repositories.ShortUrlRepo;
import com.frankc.shorturl.utils.Validator;
import com.frankc.shorturl.utils.Generator;

/**
 * ShortUrl Service layer default implementation.
 */
@Service
public class Impl implements ShortUrlService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ShortUrlRepo repo;

    @Autowired
    private Generator genShrtU;

    @Value("${com.frankc.shorturl.service.maxShortUrlPathGenRetries:3}")
    private int maxShortUrlPathGenerationRetries;

    public List<ShortUrl> findAll(final Pageable pageRequest) {
        return repo.findAll(pageRequest).getContent();
    }

    public ShortUrl find(final String shortUrlPath) throws NoSuchElementException {
        return repo.findShortUrlByShortUrlPath(shortUrlPath);
}

    public ShortUrl createShortUrl(final String redirectTo) throws IllegalArgumentException, MaxPathGenerationRetriesException {
        String fixedRedirectTo = Validator.fixUrlProtocol(redirectTo);

        try {
            new URL(fixedRedirectTo);
            Validator.validateUrl(fixedRedirectTo);
        } catch (MalformedURLException ex) {
            logger.error("Request to create shortUrl with invalid redirectTo:"
                         + fixedRedirectTo);
            throw new IllegalArgumentException();
        }

        ShortUrl newShortUrl = new ShortUrl(fixedRedirectTo);

        int n = 1;
        while (true) {
            newShortUrl.setShortUrlPath(genShrtU.generateBase62());

            try {
                return repo.save(newShortUrl);
            } catch (DataIntegrityViolationException ex) {
                logger.warn("Short URL Path generation failed to create unique path : " + n + " of " + maxShortUrlPathGenerationRetries + " attempts : " + ex);
            }
            if (n >= maxShortUrlPathGenerationRetries) { throw new MaxPathGenerationRetriesException(); }
            ++n;
        }
    }

    public void deleteByShortUrlPath(final String shortUrlPath) throws NoSuchElementException {
        if (!repo.existsByShortUrlPath(shortUrlPath)) { throw new NoSuchElementException(); }
        repo.deleteByShortUrlPath(shortUrlPath);
    }
}
