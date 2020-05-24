package org.metabrainz.mobile.presentation.features.label;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

import io.reactivex.Single;

public class LabelViewModel extends LookupViewModel {

    private LiveData<Label> labelData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, Label.class));

    public LabelViewModel() {
    }

    @Override
    public LiveData<Label> initializeData() {
        return labelData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("label", MBID, Constants.LOOKUP_LABEL_PARAMS);
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update LabelData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }
}
