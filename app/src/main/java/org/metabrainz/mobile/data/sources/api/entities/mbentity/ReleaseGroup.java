package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.Link;

import java.util.ArrayList;
import java.util.List;

public class ReleaseGroup extends MBEntity {
    private String title;
    @SerializedName("primary-type")
    private String primaryType;
    @SerializedName("secondary-types")
    private final List<String> secondaryTypes = new ArrayList<>();
    private int count;
    //TODO: Implement correct wrapper JSON
    @SerializedName("artist-credit")
    private final List<ArtistCredit> artistCredits = new ArrayList<>();

    private final List<Link> relations = new ArrayList<>();
    private final List<Release> releases = new ArrayList<>();

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases.addAll(releases);
    }

    public List<Link> getRelations() {
        return relations;
    }

    public void setRelations(List<Link> relations) {
        this.relations.addAll(relations);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ArtistCredit> getArtistCredits() {
        return artistCredits;
    }

    public void setArtistCredits(List<ArtistCredit> artistCredits) {
        this.artistCredits.addAll(artistCredits);
    }

    public List<String> getSecondaryTypes() {
        return secondaryTypes;
    }

    public void setSecondaryTypes(List<String> secondaryTypes) {
        this.secondaryTypes.addAll(secondaryTypes);
    }

    public String getFullType() {
        if (secondaryTypes.size() != 0) {
            String type = primaryType;
            for (String string : secondaryTypes) type = type.concat(" + ").concat(string);
            return type;
        } else return primaryType;
    }
}
