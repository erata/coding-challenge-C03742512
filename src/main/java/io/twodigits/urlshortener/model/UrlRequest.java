package io.twodigits.urlshortener.model;

public class UrlRequest {
    private String url;
    private String user;

    public UrlRequest() {}

    public UrlRequest(String url, String user) {
        this.url = url;
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}