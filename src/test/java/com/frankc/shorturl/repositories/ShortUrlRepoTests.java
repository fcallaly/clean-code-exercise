package com.frankc.shorturl.repositories;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.frankc.shorturl.entities.ShortUrl;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShortUrlRepoTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ShortUrlRepo shortUrlRepo;

    private final AtomicLong counter = new AtomicLong();

    @Test
    public void findAll_returnsShortUrl() {
        ShortUrl newShortUrl = createShortUrl();

        testEntityManager.persist(newShortUrl);

        List<ShortUrl> foundShortUrls = shortUrlRepo.findAll();

        assertTrue("FindAll should return 1 ShortUrl",
                   foundShortUrls.size() == 1);
        assertTrue("Invalid ShortUrlPath in ShortUrl",
                   foundShortUrls.get(0).getShortUrlPath()
                                 .equals(newShortUrl.getShortUrlPath()));
        assertTrue("Invalid RedirectTo in ShortUrl",
                   foundShortUrls.get(0).getRedirectTo()
                                 .equals(newShortUrl.getRedirectTo()));

        logger.info("Retrieved Short Url: " + foundShortUrls.get(0));
    }

    @Test
    public void deleteByShortUrlPath_removesShortUrl() {
        ShortUrl newShortUrl = createShortUrl();

        testEntityManager.persist(newShortUrl);

        List<ShortUrl> foundShortUrls = shortUrlRepo.findAll();
        assertTrue("FindAll should return 1 ShortUrl",
                   foundShortUrls.size() == 1);

        long createdId = foundShortUrls.get(0).getId();
        long deletedId = shortUrlRepo.deleteByShortUrlPath(
                                          newShortUrl.getShortUrlPath());
        assertTrue("Delete should return id of deleted object: "
                   + deletedId + " : " + createdId,
                   createdId == deletedId);

        foundShortUrls = shortUrlRepo.findAll();
        assertTrue("FindAll should return 0 ShortUrl",
                   foundShortUrls.size() == 0);
    }

    @Test
    public void existsByShortUrlPath_correctResponse() {
        ShortUrl newShortUrl = createShortUrl();

        testEntityManager.persist(newShortUrl);

        assertTrue("existsByShortUrlPath should be false",
                   !shortUrlRepo.existsByShortUrlPath("NONEXISTINGPATH"));

        assertTrue("existsByShortUrlPath should be true",
                   shortUrlRepo.existsByShortUrlPath(
                                           newShortUrl.getShortUrlPath()));
    }

    private ShortUrl createShortUrl() {
        ShortUrl newShortUrl = new ShortUrl("http://www.testdomainname"
                                            + counter.incrementAndGet()
                                            + ".co.nz");
        newShortUrl.setShortUrlPath("efgHIJK456" + counter.incrementAndGet());
        return newShortUrl;
    }
}
