package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.Alias;
import org.metabrainz.mobile.data.sources.api.entities.LifeSpan;

import java.util.ArrayList;
import java.util.List;

public class Area extends MBEntity {

    //TODO: ISO codes field to be added

    private String type;
    private String name;
    @SerializedName("sort-name")
    private String sortName;
    private final List<Alias> aliases = new ArrayList<>();
    @SerializedName("life-span")
    private LifeSpan lifeSpan;

    @NonNull
    @Override
    public String toString() {
        return "Area{" +
                "id='" + mbid + '\'' +
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

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(List<Alias> aliases) {
        this.aliases.addAll(aliases);
    }
}