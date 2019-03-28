package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;

import java.util.ArrayList;
import java.util.Iterator;

public class ReleaseGroup {
    @SerializedName("id")
    private String mbid;
    private String title;
    private String disambiguation;
    @SerializedName("primary-type")
    private String type;
    private int count;
    //TODO: Implement correct wrapper JSON
    @SerializedName("artist-credit")
    private ArrayList<ArtistCredit> artistCredits = new ArrayList<>();
    private ArrayList<Release> releases = new ArrayList<>();

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
