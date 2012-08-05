/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.loader.result;

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
