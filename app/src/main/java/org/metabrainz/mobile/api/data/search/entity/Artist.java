package org.metabrainz.mobile.api.data.search.entity;

import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("id")
    private String mbid;
    private String name;
    private String country;
    private String disambiguation;
    private String type;
    @SerializedName("sort-name")
    private String sortName;
    private Area area;
    @SerializedName("begin-area")
    private Area beginArea;
    @SerializedName("end-area")
    private Area endArea;

    public Artist(String mbid, String name, String country, String disambiguation) {
        this.mbid = mbid;
        this.name = name;
        this.country = country;
        this.disambiguation = disambiguation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getBeginArea() {
        return beginArea;
    }

    public void setBeginArea(Area beginArea) {
        this.beginArea = beginArea;
    }

    public Area getEndArea() {
        return endArea;
    }

    public void setEndArea(Area endArea) {
        this.endArea = endArea;
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

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                '}';
    }
}
