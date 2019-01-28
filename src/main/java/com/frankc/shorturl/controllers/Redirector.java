package com.frankc.shorturl.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.frankc.shorturl.controllers.exceptions.ShortUrlNotFoundException;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

/**
 * Implements redirects from a ShortUrlPath to the corresponding redirectTo URI.
 */
@RestController
@RequestMapping("/")
public class Redirector {

@Autowired
private ShortUrlService serv;

/**
 * Redirect to the redirectTo field of a ShortUrl.
 *
 * @param shortUrlPath of shortUrl to be redirected by
   * @throws ShortUrlNotFoundException
       */
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("{shortUrlPath:[a-zA-Z0-9]+$}")
    public HttpEntity<Void> redirectByShortUrl(@PathVariable("shortUrlPath") final String shortUrlPath)throws ShortUrlNotFoundException {
        ShortUrl requestedShortUrl = null;

        requestedShortUrl = serv.find(shortUrlPath);
        if (requestedShortUrl == null) { System.out.println("Error");
            throw new ShortUrlNotFoundException();
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        try { responseHeaders.setLocation( new URI(requestedShortUrl.getRedirectTo())); }
        catch (URISyntaxException ex) { System.out.println("Error");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        System.out.println("Redirecting");
        return new ResponseEntity<>(responseHeaders, HttpStatus.MOVED_PERMANENTLY);
    }
}
