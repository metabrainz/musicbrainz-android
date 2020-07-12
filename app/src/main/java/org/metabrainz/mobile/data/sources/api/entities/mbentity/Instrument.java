package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import androidx.annotation.NonNull;

import org.metabrainz.mobile.data.sources.api.entities.Alias;

import java.util.ArrayList;
import java.util.List;

public class Instrument extends MBEntity {
    private String type;
    private String name;
    private String description;
    private final List<Alias> aliases = new ArrayList<>();

    @NonNull
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

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(List<Alias> aliases) {
        this.aliases.addAll(aliases);
    }
}
