package org.metabrainz.mobile.presentation.features.release_group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

<<<<<<< HEAD
<<<<<<< HEAD
import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
=======
import org.metabrainz.mobile.data.sources.Constants;
=======
import org.metabrainz.mobile.data.repository.LookupRepository;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
<<<<<<< HEAD
import org.metabrainz.mobile.util.SingleLiveEvent;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.

import io.reactivex.Single;

public class ReleaseGroupViewModel extends LookupViewModel {

<<<<<<< HEAD
<<<<<<< HEAD
    private LiveData<WikiSummary> wikiSummary;
    private LiveData<ReleaseGroup> liveData;
=======
    private LiveData<ReleaseGroup> releaseGroupData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, ReleaseGroup.class));
    private SingleLiveEvent<WikiSummary> wikiSummary;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

    public ReleaseGroupViewModel() {
        entity = MBEntityType.RELEASE_GROUP;
=======
    private LiveData<WikiSummary> wikiSummary;
    private LiveData<ReleaseGroup> liveData;

    public ReleaseGroupViewModel() {
<<<<<<< HEAD
        entity = MBEntities.RELEASE_GROUP;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
=======
        entity = MBEntityType.RELEASE_GROUP;
>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, ReleaseGroup.class));
        wikiSummary = Transformations.switchMap(liveData, this::fetchWikiSummary);
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    public LiveData<ReleaseGroup> getData() {
        return liveData;
    }

    LiveData<WikiSummary> getWikiData() {
        return wikiSummary;
<<<<<<< HEAD
=======
    public LiveData<ReleaseGroup> initializeData() {
        return releaseGroupData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("release-group", MBID, Constants.LOOKUP_RELEASE_GROUP_PARAMS);
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    }

    Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ReleaseGroupData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    private LiveData<WikiSummary> fetchWikiSummary(ReleaseGroup releaseGroup) {
        String title = "";
        int method = -1;
        if (releaseGroup != null) {
            for (Link link : releaseGroup.getRelations()) {
                if (link.getType().equals("wikipedia")) {
                    title = link.getPageTitle();
                    method = LookupRepository.METHOD_WIKIPEDIA_URL;
                    break;
                }
                if (link.getType().equals("wikidata")) {
                    title = link.getPageTitle();
                    method = LookupRepository.METHOD_WIKIDATA_ID;
                    break;
                }
            }
        }
        return repository.fetchWikiSummary(title, method);
<<<<<<< HEAD
=======
    public void getWikiSummary(String title, int method) {
        repository.getWikiSummary(title, method);
    }

    public SingleLiveEvent<WikiSummary> initializeWikiData() {
        if (wikiSummary == null)
            wikiSummary = repository.initializeWikiData();
        return wikiSummary;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    }
}
