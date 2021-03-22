package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.LifeSpan;

public class Event extends MBEntity {

    //TODO: Add Relations Field

    private String name;
    @SerializedName("life-span")
    private LifeSpan lifeSpan;
    private String type;
    private String time;

    @NonNull
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
