package org.metabrainz.mobile.data.sources.api.entities.response.collection;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistCollectionResponse {

    @SerializedName("artist-count")
    private int count;

    @SerializedName("artist-offset")
    private int offset;

    private List<Artist> artists = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
