package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.Alias;

import java.util.ArrayList;

public class Instrument {
    @SerializedName("id")
    private String mbid;
    private String type;
    private String name;
    private String disambiguation;
    private String description;
    private ArrayList<Alias> aliases = new ArrayList<>();

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "mbid='" + mbid + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", aliases=" + aliases +
                '}';
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(ArrayList<Alias> aliases) {
        this.aliases = aliases;
    }
}
