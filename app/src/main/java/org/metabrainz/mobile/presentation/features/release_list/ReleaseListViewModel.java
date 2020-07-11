package org.metabrainz.mobile.presentation.features.release_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.List;

import io.reactivex.Single;

public class ReleaseListViewModel extends ViewModel {

    private final LookupRepository repository = LookupRepository.getRepository();
    private final MutableLiveData<List<Release>> releasesLiveData = new MutableLiveData<>();

    public ReleaseListViewModel() {
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ArtistData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    public LiveData<List<Release>> getData() {
        return releasesLiveData;
    }

    public void setData(List<Release> releases) {
        releasesLiveData.setValue(releases);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LookupRepository.destroyRepository();
    }
}
