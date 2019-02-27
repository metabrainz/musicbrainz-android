package org.metabrainz.mobile.api.data.search.entity;

import com.google.gson.annotations.SerializedName;

public class Event {

    //TODO: Add Relations Field

    @SerializedName("id")
    private String mbid;
    private String name;
    private String disambiguation;
    @SerializedName("life-span")
    private LifeSpan lifeSpan;
    private String type;
    private String time;

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                ", lifeSpan=" + lifeSpan +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                '}';
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
