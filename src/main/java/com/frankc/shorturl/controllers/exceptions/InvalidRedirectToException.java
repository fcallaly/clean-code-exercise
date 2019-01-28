package com.frankc.shorturl.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for when an invalid redirectTo URL is provided.
 * 
 * Sets HTTP status code to 400 with sensible message.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST,
                reason = "The given redirectTo is an invalid URL")
public class InvalidRedirectToException extends Exception {
    private static final long serialVersionUID = 3707122206903189124L;
}
