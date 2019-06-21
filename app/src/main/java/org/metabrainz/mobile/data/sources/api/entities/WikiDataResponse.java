package org.metabrainz.mobile.data.sources.api.entities;

import java.util.Map;

public class WikiDataResponse {

    private Map<String, WikiDataEntry> sitelinks;
    private String type;
    private String id;

    public Map<String, WikiDataEntry> getSitelinks() {
        return sitelinks;
    }

    public void setSitelinks(Map<String, WikiDataEntry> sitelinks) {
        this.sitelinks = sitelinks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
