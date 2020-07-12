package org.metabrainz.mobile.presentation.features.release_group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

import io.reactivex.Single;

public class ReleaseGroupViewModel extends LookupViewModel {

    private final LiveData<WikiSummary> wikiSummary;
    private final LiveData<ReleaseGroup> liveData;

    public ReleaseGroupViewModel() {
        entity = MBEntityType.RELEASE_GROUP;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, ReleaseGroup.class));
        wikiSummary = Transformations.switchMap(liveData, this::fetchWikiSummary);
    }

    @Override
    public LiveData<ReleaseGroup> getData() {
        return liveData;
    }

    LiveData<WikiSummary> getWikiData() {
        return wikiSummary;
    }

    Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ReleaseGroupData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

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
    }
}
