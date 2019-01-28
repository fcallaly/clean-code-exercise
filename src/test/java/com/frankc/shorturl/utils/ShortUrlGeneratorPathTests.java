package com.frankc.shorturl.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShortUrlGeneratorPathTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void generateBase62_isCreated() {
        String newShortUrl = new Generator().generateBase62();
        logger.debug("URL Generated: " + newShortUrl);

        assertThat(newShortUrl, is(not(isEmptyOrNullString())));
    }

}
