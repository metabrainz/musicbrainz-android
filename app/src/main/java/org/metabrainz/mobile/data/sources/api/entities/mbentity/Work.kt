package org.metabrainz.mobile.data.sources.api.entities.mbentity;

class Work extends MBEntity {
    private String title;
    //TODO: Implement Relations

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Work{" +
                "title='" + title + '\'' +
                ", mbid='" + mbid + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}';
    }
}
