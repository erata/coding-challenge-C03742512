package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.Visit;
import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.UrlRequest;

import java.util.List;
import java.util.Optional;

public interface URLShortenerService {
    /**
     * Get a list of all URLs that belong to a user.
     *
     * @param user
     * @return a list of users
     */
    Iterable<URL> listURLs(String user);

    /**
     * Add a new URL to the collection of URLs for a user.
     *
     * @param user
     * @param url
     * @return The URL object which has been created
     */
    URL addURL(String user, String url);

    /**
     * Get a specific URL of a user by its ID.
     * @param user
     * @param id
     * @return The URL object
     */
    Optional<URL> getURL(String user, String id);

    /**
     * Return a specific URL by ID.
     *
     * @param id
     * @return The URL object
     */
    Optional<URL> getURL(String id);

    /**
     * Update a specific URL which with id.
     * @param id
     * @param urlRequest
     */
    URL updateURL(String id, UrlRequest urlRequest);

    /**
     * Delete a specific URL which belongs to a user.
     * @param user
     * @param id
     */
    void deleteURL(String user, String id);

    /**
     * Get visit statistics of a specific shortURL with id.
     * @param id
     */
    List<Visit> getStatistics(String id);
}
