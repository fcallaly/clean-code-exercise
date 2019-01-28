package com.frankc.shorturl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.frankc.shorturl.entities.ShortUrl;

/**
 * JPA Repository interface for ShortUrl Entity.
 */
@Repository
public interface ShortUrlRepo
                 extends JpaRepository<ShortUrl, Long> {

    ShortUrl findShortUrlByShortUrlPath(String shortUrlPath);

    boolean existsByShortUrlPath(String shortUrlPath);

    @Transactional
    Long deleteByShortUrlPath(String shortUrlPath);
}
