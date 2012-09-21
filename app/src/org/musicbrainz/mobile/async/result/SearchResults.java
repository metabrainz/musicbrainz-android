package org.musicbrainz.mobile.async.result;

import java.util.List;

import org.musicbrainz.android.api.data.ArtistSearchResult;
import org.musicbrainz.android.api.data.ReleaseGroupInfo;

public class SearchResults {
    
    private final SearchType type;
    private final List<ArtistSearchResult> artistResults;
    private final List<ReleaseGroupInfo> releaseGroupResults;
    
    @SuppressWarnings("unchecked")
    public SearchResults(SearchType type, List<?> results) {
        if (type == SearchType.ARTIST) {
            artistResults = (List<ArtistSearchResult>) results;
            releaseGroupResults = null;
        } else {
            artistResults = null;
            releaseGroupResults = (List<ReleaseGroupInfo>) results;
        }
        this.type = type;
    }
    
    public SearchResults(List<ArtistSearchResult> artistResults, List<ReleaseGroupInfo> releaseGroupResults) {
        this.artistResults = artistResults;
        this.releaseGroupResults = releaseGroupResults;
        this.type = SearchType.ALL;
    }

    public SearchType getType() {
        return type;
    }

    public List<ArtistSearchResult> getArtistResults() {
        return artistResults;
    }

    public List<ReleaseGroupInfo> getReleaseGroupResults() {
        return releaseGroupResults;
    }

    public enum SearchType {
        ARTIST, RELEASE_GROUP, ALL;
    }
}
