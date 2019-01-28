package com.frankc.shorturl.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for when an attempt is made to write shortUrlPath in during an
 * operation where that field is read only.
 * 
 * Sets HTTP status code to 409 with sensible message.
 */
@ResponseStatus(code = HttpStatus.CONFLICT,
                reason = "shortUrlPath is read_only")
public class ImmutableShortUrlPathException extends RuntimeException {
    private static final long serialVersionUID = 8784771960248183750L;
}
