package org.metabrainz.mobile.data.repository;

import androidx.lifecycle.LiveData;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.Resource;
import org.metabrainz.mobile.util.SingleLiveEvent;

import io.reactivex.Single;

public interface LookupRepository {

    int METHOD_WIKIPEDIA_URL = 0;
    int METHOD_WIKIDATA_ID = 1;

    LiveData<Resource<String>> fetchData(String entity, String MBID, String params);

    SingleLiveEvent<Resource<WikiSummary>> fetchWikiSummary(String string, int method);

    Single<CoverArt> fetchCoverArtForRelease(Release release);

    LiveData<CoverArt> fetchCoverArt(String MBID);
}
