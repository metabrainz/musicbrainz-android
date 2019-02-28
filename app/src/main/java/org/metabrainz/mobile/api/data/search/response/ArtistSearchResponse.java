package org.metabrainz.mobile.api.data.search.response;

import org.metabrainz.mobile.api.data.search.entity.Artist;

import java.util.List;

public class ArtistSearchResponse extends JSONResponse {

    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
