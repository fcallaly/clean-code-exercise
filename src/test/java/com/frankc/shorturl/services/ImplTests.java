package com.frankc.shorturl.services;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.frankc.shorturl.controllers.exceptions.MaxPathGenerationRetriesException;
import com.frankc.shorturl.entities.ShortUrl;
import com.frankc.shorturl.repositories.ShortUrlRepo;
import com.frankc.shorturl.utils.Validator;
import com.frankc.shorturl.utils.Generator;

/**
 * Unit Tests for ShortUrlServiceImpl.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("nojpa")
public class ImplTests {

    private static final String TEST_SHORTURLPATH = "abcdABCD1234";

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private ShortUrlService shortUrlService;

    @MockBean
    private ShortUrlRepo mockShortUrlRepo;

    @SpyBean
    private Generator shortUrlPathGenerator;

    @Test
    public void findAll_returnsList() {
        when(mockShortUrlRepo.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<ShortUrl>(new ArrayList<ShortUrl>()));

        assertThat("findAll should return List",
                   shortUrlService.findAll(PageRequest.of(0,  20)),
                   instanceOf(List.class));
    }

    @Test
    public void findByShortUrlPath_returnsShortUrl() {
        ShortUrl testShortUrl = createShortUrl();
        when(mockShortUrlRepo
                .findShortUrlByShortUrlPath(testShortUrl.getShortUrlPath()))
             .thenReturn(testShortUrl);

        ShortUrl foundShortUrl = shortUrlService.find(
                                            testShortUrl.getShortUrlPath());

        assertThat("findByShortUrlPath should return ShortUrl",
                   foundShortUrl, instanceOf(ShortUrl.class));
        assertEquals("findByShortUrlPath should return correct data",
                     foundShortUrl.getShortUrlPath(),
                     testShortUrl.getShortUrlPath());
    }

    public void findByShortUrlPath_unknownThrowsNoSuchElem() {
        when(mockShortUrlRepo
                .findShortUrlByShortUrlPath(TEST_SHORTURLPATH))
             .thenReturn(null);

        assertNull(shortUrlService.find(TEST_SHORTURLPATH));
    }

    @Test
    public void createShortUrl_returnsShortUrl() throws MaxPathGenerationRetriesException {
        ShortUrl testShortUrl = createShortUrl();
        testShortUrl.setShortUrlPath(null);

        when(mockShortUrlRepo.save(isA(ShortUrl.class)))
             .thenReturn(testShortUrl);

        ShortUrl foundShortUrl = shortUrlService.createShortUrl(
                                            testShortUrl.getRedirectTo());

        assertThat("findByShortUrlPath should return ShortUrl",
                   foundShortUrl, instanceOf(ShortUrl.class));
        assertEquals("findByShortUrlPath should return correct data",
                     foundShortUrl.getRedirectTo(),
                     testShortUrl.getRedirectTo());
    }

    @Test
    public void createShortUrl_addsProtocolToRedirectTo() throws MaxPathGenerationRetriesException {
        ShortUrl testShortUrl = new ShortUrl("www.noprotocol.com");

        when(mockShortUrlRepo.save(isA(ShortUrl.class)))
             .thenAnswer(new Answer<ShortUrl>() {
                public ShortUrl answer(final InvocationOnMock invocation)
                                       throws Throwable {
                    Object[] args = invocation.getArguments();
                    return (ShortUrl) args[0];
                }
             });

        ShortUrl foundShortUrl = shortUrlService.createShortUrl(
                                            testShortUrl.getRedirectTo());

        assertThat("findByShortUrlPath should return ShortUrl",
                   foundShortUrl, instanceOf(ShortUrl.class));
        assertTrue("findByShortUrlPath should return correct data",
                     foundShortUrl.getRedirectTo()
                     .startsWith(Validator.DEFAULT_URL_PROTOCOL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShortUrl_invalidRedirectThrowsIllegalArg() throws MaxPathGenerationRetriesException {
        shortUrlService.createShortUrl("http://invalidport:-2");
    }

    @Test(expected = MaxPathGenerationRetriesException.class)
    public void createShortUrl_conflictReturnsMaxRetriesException() throws MaxPathGenerationRetriesException {
        when(mockShortUrlRepo.save(isA(ShortUrl.class)))
             .thenThrow(new DataIntegrityViolationException("Conflict"));

        shortUrlService.createShortUrl("http://www.redirectTo.com");
    }

    @Test
    public void deleteByShortUrlPath_noErrors() {
        when(mockShortUrlRepo
                .existsByShortUrlPath(TEST_SHORTURLPATH))
             .thenReturn(true);

        shortUrlService.deleteByShortUrlPath(TEST_SHORTURLPATH);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteByShortUrlPath_unknownThrowsNoSuchElem() {
        when(mockShortUrlRepo
                .existsByShortUrlPath(TEST_SHORTURLPATH))
             .thenReturn(false);

        shortUrlService.deleteByShortUrlPath(TEST_SHORTURLPATH);
    }

    private ShortUrl createShortUrl() {
        ShortUrl newShortUrl = new ShortUrl("http://www.testdomainname"
                                            + counter.incrementAndGet()
                                            + ".co.nz");
        newShortUrl.setShortUrlPath("efgHIJK456" + counter.incrementAndGet());
        return newShortUrl;
    }
}
