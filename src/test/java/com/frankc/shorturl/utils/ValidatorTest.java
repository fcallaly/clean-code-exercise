package com.frankc.shorturl.utils;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Validator validator = new Validator();

    @Test
    public void fixUrlProtocol_withProtocol() {
        String result;
        String url = "http://www.google.ie";

        logger.debug("Test standard url string:");
        result = validator.fixUrlProtocol(url);
        logger.debug(result);
        assertTrue(result.equals(url));
    }

    @Test
    public void fixUrlProtocol_noProtocol() {
        String result;
        String url = "www.google.ie";
        logger.debug("Test url string with no protcol:");
        result = validator.fixUrlProtocol(url);
        logger.debug(result);
        assertTrue(result.equals("http://" + url));
    }

    @Test
    public void fixUrlProtocol_caseInsensitivity() {
        String result;
        String url = "HTtP://www.google.ie";
        logger.debug("Test url string with changing case protocol:");
        result = validator.fixUrlProtocol(url);
        logger.debug(result);
        assertTrue(result.equalsIgnoreCase(url));
    }

    @Test
    public void validateUrl_success() throws MalformedURLException {
        logger.debug("Test url string is valid");
        validator.validateUrl("http://www.blah.com/some/crazy/"
                                    + "path.html?param1=foo&param2=bar");
    }

    @Test(expected = MalformedURLException.class)
    public void validateUrl_failure() throws MalformedURLException {
        logger.debug("Test url string is invalid");
        validator.validateUrl("http://mw1.google.com/mw-earth-vectordb"
            + "/kml-samples/gp/seattle/gigapxl/$[level]/r$[y]_c$[x].jpg\r\n");
    }
}
