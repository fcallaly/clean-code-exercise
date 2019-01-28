package com.frankc.shorturl.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * Domain object for ShortUrl, primarily maps Short URL Paths to Long URLs.
 */
@Entity
public class ShortUrl {

    private static final int UUID2_FIELD_SIZE = 36;

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Column(unique = true, length = UUID2_FIELD_SIZE)
    private String shortUrlPath;

    @Lob
    private String redirectTo;


                        
                            
                        
    public String getShortUrlPath() {
        return shortUrlPath;
    }

    public void setShortUrlPath(String path) {
        this.shortUrlPath = path;
    }


    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;


    @JsonIgnore
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public ShortUrl() { }

    public ShortUrl(final String to) {
        this.setRedirectTo(to);
    }


    public String getRedirectTo() {
        return this.redirectTo;
    }

    public void setRedirectTo(String to) {
        this.redirectTo = to;
    }

    @ApiModelProperty(hidden = true)
    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
       this.created = created;
    }

    @ApiModelProperty(hidden = true)
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String toString() {
        return "Id: " + this.getId() + ", shortUrl: " + this.getShortUrlPath() + ", redirectTo: " + this.getRedirectTo() + ", created: " + this.getCreated()+ ", last updated: " + this.getLastUpdated();
    }
}
