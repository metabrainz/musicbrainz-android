package org.metabrainz.mobile.api.data.search.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Area implements Serializable {

    //TODO: ISO codes field to be added

    @SerializedName("id")
    private String id;
    private String type;
    private String name;
    @SerializedName("sort-name")
    private String sortName;
    private ArrayList<Alias> aliases = new ArrayList<>();
    private String disambiguation;
    @SerializedName("life-span")
    private LifeSpan lifeSpan;

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", sortName='" + sortName + '\'' +
                ", aliases=" + aliases +
                ", disambiguation='" + disambiguation + '\'' +
                ", lifeSpan=" + lifeSpan +
                '}';
    }

    public LifeSpan getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(LifeSpan lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public ArrayList<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(ArrayList<Alias> aliases) {
        this.aliases = aliases;
    }

    public String getdisambiguation() {
        return disambiguation;
    }

    public void setdisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }
}