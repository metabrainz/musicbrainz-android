package org.metabrainz.mobile.presentation.features.label;

import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

import io.reactivex.Single;

public class LabelViewModel extends LookupViewModel {

    public LabelViewModel() {
        entity = MBEntities.LABEL;
        liveData = Transformations.map(repository.initializeData(),
                data -> new Gson().fromJson(data, Label.class));
    }

    Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update LabelData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }
}
