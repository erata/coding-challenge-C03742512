package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.StatisticsResponse;
import io.twodigits.urlshortener.model.Visit;
import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.UrlRequest;
import io.twodigits.urlshortener.repository.URLRepository;
import io.twodigits.urlshortener.service.URLShortenerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
        //produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
        //consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class URLShortenerController {

    private final URLShortenerService urlService;
    private final URLRepository repository;

    public URLShortenerController(URLShortenerService urlService, URLRepository repository) {
        this.urlService = urlService;
        this.repository = repository;
    }

    @GetMapping("/urls")
    public ResponseEntity<Iterable<URL>> listURLs(@RequestParam(value = "user") String user) {
        Iterable<URL> urls = urlService.listURLs(user);
        return ResponseEntity.ok(urls);
    }

    @PostMapping("/urls")
    public ResponseEntity<URL> addURL( @RequestBody UrlRequest urlRequest) {
        URL url = urlService.addURL(urlRequest.getUser(), urlRequest.getUrl());

        if (url != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(url);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/urls/{id}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable("id") String id,
                                                      @RequestParam(value = "user") String user,
                                                      HttpServletRequest request) {
        Optional<URL> urlOptional = urlService.getURL(user, id);

        if(urlOptional.isPresent()) {
            URL url = urlOptional.get();
            url.addVisit(createVisit(url, request));
            repository.save(url);

            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .header(HttpHeaders.LOCATION, url.getURL())
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/urls/{id}")
    public ResponseEntity<URL> updateUrlById(@PathVariable String id, @RequestBody UrlRequest urlRequest) {
        URL url = urlService.updateURL(id, urlRequest);
        if (url == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(url);
        }
    }

    @DeleteMapping("/urls/{id}")
    public ResponseEntity<Void> deleteURL(@PathVariable("id") String id, @RequestParam(value = "user") String user) {
        urlService.deleteURL(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/urls/{id}/statistics")
    public ResponseEntity<StatisticsResponse> getUrlStatistics(@PathVariable String id) {
        Optional<URL> url = repository.findById(id);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        List<Visit> visits = url.get().getVisits();
        long uniqVisitCount = visits.stream()
                .map(Visit::getIpAddress)
                .distinct()
                .count();

        StatisticsResponse statisticsResponse = new StatisticsResponse(visits.size(), uniqVisitCount, visits);

        return ResponseEntity.ok(statisticsResponse);
    }

    private Visit createVisit(URL url, HttpServletRequest request) {
        Visit visit = new Visit();
        visit.setUrl(url);
        visit.setUserAgent(request.getHeader("User-Agent"));
        visit.setReferrer(request.getHeader("Referer"));
        visit.setIpAddress(getIpAddress(request));
        return visit;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
