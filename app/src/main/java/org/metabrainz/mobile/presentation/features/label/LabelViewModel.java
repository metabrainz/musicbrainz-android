package org.metabrainz.mobile.presentation.features.label;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LabelLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import io.reactivex.Single;

public class LabelViewModel extends ViewModel {

    private LabelLookupRepository repository = LabelLookupRepository.getRepository();
    private MutableLiveData<Label> labelData;
    private String MBID;

    public LabelViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public MutableLiveData<Label> initializeLabelData() {
        // Obtain live data from the repository if not already present
        if (labelData == null)
            labelData = repository.initializeLabelData();
        return labelData;
    }

    public void getLabelData() {
        // Call the repository to query the database to update the label data
        repository.getLabel(MBID);
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update LabelData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LabelLookupRepository.destroyRepository();
    }
}
