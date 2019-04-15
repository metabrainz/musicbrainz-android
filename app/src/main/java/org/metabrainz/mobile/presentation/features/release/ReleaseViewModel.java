package org.metabrainz.mobile.presentation.features.release;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.ReleaseLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

public class ReleaseViewModel extends ViewModel {

    private ReleaseLookupRepository repository = ReleaseLookupRepository.getRepository();
    private MutableLiveData<Release> releaseData;
    private MutableLiveData<CoverArt> coverArtData;
    private String MBID;

    public ReleaseViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public MutableLiveData<Release> initializeReleaseData() {
        // Obtain live data from the repository if not already present
        if (releaseData == null)
            releaseData = repository.initializeReleaseLiveData();
        return releaseData;
    }

    public void getReleaseData() {
        // Call the repository to query the database to update the release data
        repository.getRelease(MBID);
    }

    public MutableLiveData<CoverArt> initializeCoverArtData() {
        if (coverArtData == null)
            coverArtData = repository.initializeCoverArtData();
        return coverArtData;
    }

    public void getCoverArtData() {
        repository.getCoverArt(MBID);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ReleaseLookupRepository.destroyRepository();
    }
}
