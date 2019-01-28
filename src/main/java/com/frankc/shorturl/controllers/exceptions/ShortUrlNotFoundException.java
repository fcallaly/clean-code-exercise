package com.frankc.shorturl.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for when ShortURL not found.
 * 
 * Sets HTTP status code to 404 with sensible message.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Short URL Not Found")
public class ShortUrlNotFoundException extends Exception {
    private static final long serialVersionUID = 8784771960248183750L;
}
