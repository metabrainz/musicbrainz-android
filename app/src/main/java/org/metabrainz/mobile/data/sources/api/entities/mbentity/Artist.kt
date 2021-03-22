package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.LifeSpan;
import org.metabrainz.mobile.data.sources.api.entities.Link;

import java.util.ArrayList;
import java.util.List;

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

    private final List<Link> relations = new ArrayList<>();
    private final List<Release> releases = new ArrayList<>();

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases.addAll(releases);
    }

    public void setRelease(Release release, int position) {
        this.releases.set(position, release);
    }

    public List<Link> getRelations() {
        return relations;
    }

    public void setRelations(List<Link> relations) {
        this.relations.addAll(relations);
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

    @NonNull
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
