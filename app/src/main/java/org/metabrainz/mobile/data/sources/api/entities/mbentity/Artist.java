package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.LifeSpan;
import org.metabrainz.mobile.data.sources.api.entities.Link;

import java.util.ArrayList;

public class Artist extends MBEntity {

    private String name;
    private String country;
    private String type;
    @SerializedName("sort-name")
    private String sortName;
    private Area area;
    @SerializedName("begin-area")
    private Area beginArea;
    @SerializedName("end-area")
    private Area endArea;
    @SerializedName("life-span")
    private LifeSpan lifeSpan;
    private String gender;

    private ArrayList<Link> relations = new ArrayList<>();
    private ArrayList<Release> releases = new ArrayList<>();

    public Artist(String mbid, String name, String country, String disambiguation) {
        this.mbid = mbid;
        this.name = name;
        this.country = country;
        this.disambiguation = disambiguation;
    }

    public ArrayList<Release> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<Release> releases) {
        this.releases = releases;
    }

    public void setRelease(Release release, int position) {
        this.releases.set(position, release);
    }

    public ArrayList<Link> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<Link> relations) {
        this.relations = relations;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LifeSpan getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(LifeSpan lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
