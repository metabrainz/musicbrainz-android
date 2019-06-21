package org.metabrainz.mobile.data.sources.api.entities.mbentity;

public class Work extends MBEntity {
    private String title;
    //TODO: Implement Relations

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
