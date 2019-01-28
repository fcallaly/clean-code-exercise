package com.frankc.shorturl.entities;


import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit Tests for ShortUrl Entity.
 */
public class ShortUrlTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long TEST_ID = 10;
    private static final String TEST_SHORTURLPATH = "abcdABCD1234";
    private static final String TEST_REDIRECTTO = "http://www.google.ie";
    private static final Date TEST_DATE = new Date();

    @Test
    public void defaultConstructor_settersAndGetters() throws Exception {
        ShortUrl newShortUrl = new ShortUrl();
        newShortUrl.setId(TEST_ID);
        newShortUrl.setShortUrlPath(TEST_SHORTURLPATH);
        newShortUrl.setRedirectTo(TEST_REDIRECTTO);
        newShortUrl.setCreated(TEST_DATE);
        newShortUrl.setLastUpdated(TEST_DATE);

        logger.info("Testing ShortUrl Object: " + newShortUrl);

        assertTrue("Invalid shortUrl Id returned",
                newShortUrl.getId() == TEST_ID);
        assertTrue("Invalid shortUrlPath returned",
                   newShortUrl.getShortUrlPath().equals(TEST_SHORTURLPATH));
        assertTrue("Invalid redirectTo returned",
                newShortUrl.getRedirectTo().equals(TEST_REDIRECTTO));
        assertTrue("Invalid createdDate returned",
                newShortUrl.getCreated().compareTo(TEST_DATE)  == 0);
        assertTrue("Invalid createdDate returned",
                newShortUrl.getLastUpdated().compareTo(TEST_DATE)  == 0);
    }

    @Test
    public void redirectToConstructor_settersAndGetters() throws Exception {
        ShortUrl newShortUrl = new ShortUrl(TEST_REDIRECTTO);
        newShortUrl.setShortUrlPath(TEST_SHORTURLPATH);

        assertTrue("Invalid shortUrlPath returned",
                   newShortUrl.getShortUrlPath().equals(TEST_SHORTURLPATH));
        assertTrue("Invalid redirectTo returned",
                newShortUrl.getRedirectTo().equals(TEST_REDIRECTTO));
    }
}
