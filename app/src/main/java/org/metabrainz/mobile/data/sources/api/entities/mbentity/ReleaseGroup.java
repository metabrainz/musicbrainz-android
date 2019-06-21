package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.Link;

import java.util.ArrayList;
import java.util.Iterator;

public class ReleaseGroup extends MBEntity {
    private String title;
    @SerializedName("primary-type")
    private String primaryType;
    @SerializedName("secondary-types")
    private ArrayList<String> secoondaryTypes = new ArrayList<>();
    private int count;
    //TODO: Implement correct wrapper JSON
    @SerializedName("artist-credit")
    private ArrayList<ArtistCredit> artistCredits = new ArrayList<>();
    private ArrayList<Link> relations = new ArrayList<>();
    private ArrayList<Release> releases = new ArrayList<>();

    public ArrayList<Link> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<Link> relations) {
        this.relations = relations;
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

    public ArrayList<ArtistCredit> getArtistCredits() {
        return artistCredits;
    }

    public void setArtistCredits(ArrayList<ArtistCredit> artistCredits) {
        this.artistCredits = artistCredits;
    }

    public ArrayList<Release> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<Release> releases) {
        this.releases = releases;
    }

    public ArrayList<String> getSecoondaryTypes() {
        return secoondaryTypes;
    }

    public void setSecoondaryTypes(ArrayList<String> secoondaryTypes) {
        this.secoondaryTypes = secoondaryTypes;
    }

    public String getFullType() {
        if (secoondaryTypes.size() != 0) {
            String type = primaryType;
            for (String string : secoondaryTypes) type = type.concat(" + ").concat(string);
            return type;
        } else return primaryType;
    }

    public String getDisplayArtist() {
        StringBuilder builder = new StringBuilder();
        Iterator<ArtistCredit> iterator = artistCredits.iterator();
        while (iterator.hasNext()) {
            ArtistCredit credit = iterator.next();
            if (credit != null && credit.getName() != null && !credit.getName().equalsIgnoreCase("null")) {
                builder.append(credit.getName());
                if (iterator.hasNext())
                    builder.append(credit.getJoinphrase());
            }
        }
        return builder.toString();
    }
}
