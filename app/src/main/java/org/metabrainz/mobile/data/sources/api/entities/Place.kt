package org.metabrainz.mobile.data.sources.api.entities;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Area;

public class Place {
    @SerializedName("id")
    private String mbid;
    private String name;
    private String disambiguation;
    private String type;
    private Area area;
    @SerializedName("life-span")
    private LifeSpan lifeSpan;
    private Coordinates coordinates;

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "mbid='" + mbid + '\'' +
                ", name='" + name + '\'' +
                ", disambiguation='" + disambiguation + '\'' +
                ", type='" + type + '\'' +
                ", area=" + area +
                ", lifeSpan=" + lifeSpan +
                ", coordinates=" + coordinates +
                '}';
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

    public LifeSpan getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(LifeSpan lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public static class Coordinates {
        private String latitude;
        private String longitude;

        @NonNull
        @Override
        public String toString() {
            return "Coordinates{" +
                    "latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    '}';
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
