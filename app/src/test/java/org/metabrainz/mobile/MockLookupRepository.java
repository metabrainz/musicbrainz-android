package org.metabrainz.mobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.Resource;
import org.metabrainz.mobile.util.SingleLiveEvent;

import io.reactivex.Single;

import static org.metabrainz.mobile.EntityTestUtils.loadResourceAsString;

public class MockLookupRepository implements LookupRepository {

    public MockLookupRepository() {
    }

    @Override
    public LiveData<Resource<String>> fetchData(String entity, String MBID, String params) {
        MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        String response = loadResourceAsString(entity + "_lookup.json");
        Resource<String> resource = new Resource<>(Resource.Status.SUCCESS, response);
        data.setValue(resource);
        return data;
    }

    @Override
    public SingleLiveEvent<Resource<WikiSummary>> fetchWikiSummary(String string, int method) {
        SingleLiveEvent<Resource<WikiSummary>> wiki = new SingleLiveEvent<>();
        String response = loadResourceAsString("artist_wiki.json");
        WikiSummary summary = new Gson().fromJson(response, WikiSummary.class);
        Resource<WikiSummary> resource = new Resource<>(Resource.Status.SUCCESS, summary);
        wiki.setValue(resource);
        return wiki;
    }

    @Override
    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        return null;
    }

    @Override
    public LiveData<CoverArt> fetchCoverArt(String MBID) {
        return null;
    }
}
