package org.musicbrainz.mobile.async.result;

import java.util.List;

import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;

public class SearchResults {
    
    private final SearchType type;
    private final List<ArtistSearchStub> artistResults;
    private final List<ReleaseGroupStub> releaseGroupResults;
    
    @SuppressWarnings("unchecked")
    public SearchResults(SearchType type, List<?> results) {
        if (type == SearchType.ARTIST) {
            artistResults = (List<ArtistSearchStub>) results;
            releaseGroupResults = null;
        } else {
            artistResults = null;
            releaseGroupResults = (List<ReleaseGroupStub>) results;
        }
        this.type = type;
    }
    
    public SearchResults(List<ArtistSearchStub> artistResults, List<ReleaseGroupStub> releaseGroupResults) {
        this.artistResults = artistResults;
        this.releaseGroupResults = releaseGroupResults;
        this.type = SearchType.ALL;
    }

    public SearchType getType() {
        return type;
    }

    public List<ArtistSearchStub> getArtistResults() {
        return artistResults;
    }

    public List<ReleaseGroupStub> getReleaseGroupResults() {
        return releaseGroupResults;
    }

    public enum SearchType {
        ARTIST, RELEASE_GROUP, ALL;
    }
}
