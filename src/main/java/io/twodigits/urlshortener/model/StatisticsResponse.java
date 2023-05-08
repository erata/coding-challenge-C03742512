package io.twodigits.urlshortener.model;

import java.util.ArrayList;
import java.util.List;

public class StatisticsResponse {
    /**
     * The number of all visits
     */
    private long numberOfAllVisits;

    /**
     * The number of uniq visits
     */
    private long numberOfUniqVisits;

    /**
     * The visits of shortUrl
     */
    private List<Visit> visits = new ArrayList<>();

    public StatisticsResponse(long numberOfAllVisits, long numberOfUniqVisits, List<Visit> visits) {
        this.numberOfUniqVisits = numberOfUniqVisits;
        this.numberOfAllVisits = numberOfAllVisits;
        this.visits = visits;
    }

    public long getNumberOfAllVisits() {
        return numberOfAllVisits;
    }

    public void setNumberOfAllVisits(int numberOfAllVisits) {
        this.numberOfAllVisits = numberOfAllVisits;
    }

    public long getNumberOfUniqVisits() {
        return numberOfUniqVisits;
    }

    public void setNumberOfUniqVisits(int numberOfUniqVisits) {
        this.numberOfUniqVisits = numberOfUniqVisits;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

}
