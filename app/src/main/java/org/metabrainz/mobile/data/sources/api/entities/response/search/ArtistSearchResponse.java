package org.metabrainz.mobile.data.sources.api.entities.response.search;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;

import java.util.List;

public class ArtistSearchResponse extends JSONSearchResponse {

    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
