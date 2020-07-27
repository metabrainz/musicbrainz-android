package org.metabrainz.mobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.util.SingleLiveEvent;

import static org.metabrainz.mobile.EntityTestUtils.loadResourceAsString;

public class MockLookupRepository extends LookupRepository {

    @Override
    public LiveData<String> fetchData(String entity, String MBID, String params) {
        MutableLiveData<String> data = new MutableLiveData<>();
        String response = loadResourceAsString(entity + "_lookup.json");
        data.setValue(response);
        return data;
    }

    @Override
    public SingleLiveEvent<WikiSummary> fetchWikiSummary(String string, int method) {
        SingleLiveEvent<WikiSummary> wiki = new SingleLiveEvent<>();
        String response = loadResourceAsString("artist_wiki.json");
        WikiSummary summary = new Gson().fromJson(response, WikiSummary.class);
        wiki.setValue(summary);
        return wiki;
    }
}
