package com.frankc.shorturl.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.services.ShortUrlService;

/**
 * Unit Tests for ShortUrlRedirectController.
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("nojpa")
public class RedirectorTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortUrlService mockShortUrlService;

    @Test
    public void findAllShortUrls_returnsMovedPermanently() throws Exception {
        when(mockShortUrlService.find("abcdABCD1234"))
            .thenReturn(new ShortUrl("http://www.redirectto.ie"));

        this.mockMvc
                .perform(get("/"
                             + "abcdABCD1234"))
                .andExpect(status().isMovedPermanently());
    }

    @Test
    public void findAllShortUrls_unknownReturnsNotFound() throws Exception {
        when(mockShortUrlService.find("abcdABCD1234"))
            .thenThrow(new NoSuchElementException());

        this.mockMvc
                .perform(get("/"
                             + "abcdABCD1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllShortUrls_invalidRedirectReturnsUnprocessable()
                                                         throws Exception {
        when(mockShortUrlService.find("abcdABCD1234"))
            .thenReturn(new ShortUrl("spaces 1nval1D"));

        this.mockMvc
                .perform(get("/"
                             + "abcdABCD1234"))
                .andExpect(status().isUnprocessableEntity());
    }
}
