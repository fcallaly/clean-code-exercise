package com.frankc.shorturl.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.frankc.shorturl.controllers.exceptions.ImmutableShortUrlPathException;
import com.frankc.shorturl.controllers.exceptions.InvalidRedirectToException;
import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
import com.frankc.shorturl.controllers.exceptions.ShortUrlNotFoundException;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

/**
 * Implements RESTful HTTP/HAL+JSON interface to ShortUrl Repository.
 */
@RestController
@RequestMapping("/short-urls/")
public class MainController {

    @Autowired
    private ShortUrlService shortUrlService;

    @Value("${com.frankc.shorturl.controller.maxPageSize:50}")
    private int maxPageSize;

    /**
     * Find a collection of all ShortUrls in the repository.
     *
     * @return a List of ShortUrls
     */
    @GetMapping(produces = "application/hal+json")
    public HttpEntity<List<ShortUrl>> findAllShortUrls(
                @RequestParam(defaultValue = "0", required = false) final Integer pageNumber,
                @RequestParam(defaultValue = "10", required = false) Integer pageSize) throws ShortUrlNotFoundException {
        if (pageSize < 0 || pageNumber < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (pageSize > maxPageSize) {
            pageSize = maxPageSize;
        }

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

        List<ShortUrl> collection =
                shortUrlService.findAll(pageRequest);

        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    /**
     * Find a single particular ShortUrl in the repository.
     *
     * @param shortUrlPath of ShortUrl to find
     * @return a ShortUrl corresponding to the given shortUrlPath
     */
    @GetMapping(path = "{shortUrlPath}", produces = "application/hal+json")
    public HttpEntity<ShortUrl> findShortUrl(@PathVariable("shortUrlPath") final String shortUrlPath)
                        throws ShortUrlNotFoundException {
                   
        try {             
        ShortUrl thisOne = shortUrlService.find(shortUrlPath);  
                              
        if (thisOne == null)throw new ShortUrlNotFoundException() ;
                                 
        return new ResponseEntity<>(thisOne, HttpStatus.OK);                             
        }                                                        
        catch (NoSuchElementException ex) { throw new ShortUrlNotFoundException(); }           
    }

    /**
     * Create a new ShortUrl and add to the repository.
     *
     * @param newShortUrl ShortUrl corresponding to data sent in request body
     * @return a ShortUrl for the object that has been added
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json",
                 produces = "application/hal+json")
    public HttpEntity<ShortUrl> createShortUrl(@RequestBody final ShortUrl newShortUrl)throws MaxPathGenerationRetriesException,
                                          InvalidRedirectToException,
                                          ShortUrlNotFoundException {
        if (newShortUrl.getShortUrlPath() != null) {
            throw new ImmutableShortUrlPathException();
        }

        ShortUrl createdShortUrl = null;
        try {
            createdShortUrl = shortUrlService
                                .createShortUrl(newShortUrl.getRedirectTo());
        } catch (IllegalArgumentException ex) {
            throw new InvalidRedirectToException();
        }

        return new ResponseEntity<ShortUrl>(createdShortUrl,
                        HttpStatus.CREATED);
    }

    /**
     * Delete a single particular ShortUrl from the repository.
     *
     * @param shortUrlPath of ShortUrl to delete
     * @return a ShortUrl corresponding to the given shortUrlPath
     */
    @DeleteMapping("{shortUrlPath}")
    public void deleteShortUrl(@PathVariable("shortUrlPath") final String shortUrlPath)
    throws ShortUrlNotFoundException {
        try {
            shortUrlService.deleteByShortUrlPath(shortUrlPath);
        } catch (NoSuchElementException ex) {
            throw new ShortUrlNotFoundException();
        }
    }
}
