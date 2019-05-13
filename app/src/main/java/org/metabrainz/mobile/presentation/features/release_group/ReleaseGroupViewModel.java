package org.metabrainz.mobile.presentation.features.release_group;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.ReleaseGroupLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.ArtistWikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.SingleLiveEvent;

import io.reactivex.Single;

public class ReleaseGroupViewModel extends ViewModel {

    private ReleaseGroupLookupRepository repository = ReleaseGroupLookupRepository.getRepository();
    private MutableLiveData<ReleaseGroup> releaseGroupData;
    private SingleLiveEvent<ArtistWikiSummary> wikiSummary;
    private String MBID;

    public ReleaseGroupViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public MutableLiveData<ReleaseGroup> initializeReleaseGroupData() {
        // Obtain live data from the repository if not already present
        if (releaseGroupData == null)
            releaseGroupData = repository.initializeReleaseGroupData();
        return releaseGroupData;
    }

    public void getReleaseGroupData() {
        // Call the repository to query the database to update the releaseGroup data
        repository.getReleaseGroup(MBID);
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ReleaseGroupData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    public void getWikiSummary(String title, int method) {
        repository.getWikiSummary(title, method);
    }

    public SingleLiveEvent<ArtistWikiSummary> initializeWikiData() {
        if (wikiSummary == null)
            wikiSummary = repository.initializeWikiData();
        return wikiSummary;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ReleaseGroupLookupRepository.destroyRepository();
    }
}
