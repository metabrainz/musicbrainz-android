package org.metabrainz.mobile.api.data.search.entity;

import com.google.gson.annotations.SerializedName;

public class Label {
    @SerializedName("id")
    private String mbid;
    private String name;
    private String disambiguation;
    private String type;
    @SerializedName("life-span")
    private LifeSpan lifeSpan;
    private String country;
    private Area area;

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    @Override
    public String toString() {
        return "Label{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                ", lifeSpan=" + lifeSpan +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", area=" + area +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdisambiguation() {
        return disambiguation;
    }

    public void setdisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public LifeSpan getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(LifeSpan lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
