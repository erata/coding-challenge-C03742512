package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.Visit;
import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.UrlRequest;
import io.twodigits.urlshortener.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class URLShortenerServiceImpl implements URLShortenerService {
    /**
     * Alphanumeric chars for shortUrl
     */
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * shortUrl length
     */
    private static final int URL_LENGTH = 7;

    @Autowired
    private URLRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<URL> listURLs(String user) {
        return repository.findByUser(user);
    }

    @Override
    @Transactional
    public URL addURL(String user, String url) {
        String id = generateUniqueUrl();
        URL newURL = new URL(id, url, user);

        repository.save(newURL);
        return newURL;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<URL> getURL(String user, String id) {
        return repository.findByIdAndUser(id, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<URL> getURL(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public URL updateURL(String id, UrlRequest urlRequest) {
        URL existingUrl = repository.findById(id).orElse(null);
        if (existingUrl == null) {
            return null;
        }

        existingUrl.setUrl(urlRequest.getUrl());
        existingUrl.setUser(urlRequest.getUser());

        return repository.save(existingUrl);
    }

    @Override
    @Transactional
    public void deleteURL(String user, String id) {
        Optional<URL> url = repository.findByIdAndUser(id, user);
        if(url.isPresent()) {
            repository.delete(url.get());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visit> getStatistics(String id) {
        Optional<URL> url = repository.findById(id);
        if (url.isPresent()) {
            return url.get().getVisits();
        }
        return null;

    }

    private String generateUniqueUrl() {
        String shortUrl = null;
        do {
            // Generate a random alphanumeric string
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < URL_LENGTH; i++) {
                int randomIndex = (int) (Math.random() * ALPHANUMERIC_CHARS.length());
                sb.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
            }
            shortUrl = sb.toString();
        } while (repository.findById(shortUrl).isPresent());

        return shortUrl;
    }
}
