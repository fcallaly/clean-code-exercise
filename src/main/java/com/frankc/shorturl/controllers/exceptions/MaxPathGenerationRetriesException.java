package com.frankc.shorturl.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for when an invalid redirectTo URL is provided.
 * 
 * Sets HTTP status code to 400 with sensible message.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR,
                reason = "Unable to generate Short Url Path, "
                         + "max retries exceeded")
public class MaxPathGenerationRetriesException extends Exception {
    private static final long serialVersionUID = 8619903128373830788L;
}
