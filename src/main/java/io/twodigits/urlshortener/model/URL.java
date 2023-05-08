package io.twodigits.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class URL {
    /**
     * The unique ID of an URL
     */
    @Id
    private String id;

    /**
     * The URL for which a short URL is provided
     */
    @Column(nullable = false)
    private String url;

    /**
     * The ID of a user to which this URL belongs
     */
    private String user;

    /**
     * The list of visits for this URL
     */
    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Visit> visits = new ArrayList<>();

    public URL() {
    }

    public URL(String id, String url, String user) {
        this.id = id;
        this.url = url;
        this.user = user;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURL() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit) {
        this.visits.add(visit);
    }

}
