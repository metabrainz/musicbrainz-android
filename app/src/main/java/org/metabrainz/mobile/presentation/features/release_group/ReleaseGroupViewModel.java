package org.metabrainz.mobile.presentation.features.release_group;

import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.SingleLiveEvent;

import io.reactivex.Single;

public class ReleaseGroupViewModel extends LookupViewModel {

    private SingleLiveEvent<WikiSummary> wikiSummary;

    public ReleaseGroupViewModel() {
        entity = MBEntities.RELEASE_GROUP;
        liveData = Transformations.map(repository.initializeData(),
                data -> new Gson().fromJson(data, ReleaseGroup.class));
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ReleaseGroupData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    public void getWikiSummary(String title, int method) {
        repository.getWikiSummary(title, method);
    }

    public SingleLiveEvent<WikiSummary> initializeWikiData() {
        if (wikiSummary == null)
            wikiSummary = repository.initializeWikiData();
        return wikiSummary;
    }
}
