package org.musicbrainz.mobile.loader;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.ReleaseGroupStub;

public class SearchResults {
    
    private final SearchType type;
    private final LinkedList<ArtistStub> artistResults;
    private final LinkedList<ReleaseGroupStub> releaseGroupResults;
    
    @SuppressWarnings("unchecked")
    public SearchResults(SearchType type, LinkedList<?> results) {
        if (type == SearchType.ARTIST) {
            artistResults = (LinkedList<ArtistStub>) results;
            releaseGroupResults = null;
        } else {
            artistResults = null;
            releaseGroupResults = (LinkedList<ReleaseGroupStub>) results;
        }
        this.type = type;
    }
    
    public SearchResults(LinkedList<ArtistStub> artistResults, LinkedList<ReleaseGroupStub> releaseGroupResults) {
        this.artistResults = artistResults;
        this.releaseGroupResults = releaseGroupResults;
        this.type = SearchType.ALL;
    }

    public SearchType getType() {
        return type;
    }

    public LinkedList<ArtistStub> getArtistResults() {
        return artistResults;
    }

    public LinkedList<ReleaseGroupStub> getReleaseGroupResults() {
        return releaseGroupResults;
    }

    public enum SearchType {
        ARTIST, RELEASE_GROUP, ALL;
    }
}
