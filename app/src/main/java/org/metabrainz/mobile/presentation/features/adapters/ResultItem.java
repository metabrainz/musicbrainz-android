package org.metabrainz.mobile.presentation.features.adapters;

public class ResultItem {
    private String MBID;
    private String name;
    private String disambiguation;
    private String primary;
    private String secondary;

    public ResultItem(String MBID, String name, String disambiguation, String primary, String secondary) {
        this.MBID = MBID;
        this.name = name;
        this.disambiguation = disambiguation;
        this.primary = primary;
        this.secondary = secondary;
    }

    public String getMBID() {
        return MBID;
    }

    public void setMBID(String MBID) {
        this.MBID = MBID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
}
