package org.metabrainz.mobile.data.sources.api.entities;

public class Link {
    private String type;
    private Url url;
    private boolean ended;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getPageTitle() {
        String resource = url.getResource().trim();
        return resource.substring(resource.lastIndexOf("/") + 1);
    }
}
