package com.frankc.shorturl.utils;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

public class Validator {

    public static final UrlValidator URL_VALIDATOR = new UrlValidator();

    public static final String DEFAULT_URL_PROTOCOL = "http://";

    private static final Pattern URL_WITH_PROTOCOL =
            Pattern.compile("(?i)^(https?|ftp|file)://.*$");

    public static String fixUrlProtocol(final String url) {
        if (URL_WITH_PROTOCOL.matcher(url).matches()) {
            return url;
        }
        return DEFAULT_URL_PROTOCOL + url;
    }

    public static void validateUrl(final String url)
                            throws MalformedURLException {
        if (!URL_VALIDATOR.isValid(url)) {
            throw new MalformedURLException();
        }
    }
}
