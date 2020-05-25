package org.metabrainz.mobile.presentation.features.label;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
<<<<<<< HEAD

import com.google.gson.Gson;

=======

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.Constants;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

import io.reactivex.Single;

public class LabelViewModel extends LookupViewModel {

<<<<<<< HEAD
<<<<<<< HEAD
    private LiveData<Label> liveData;
=======
    private LiveData<Label> labelData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, Label.class));
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

    public LabelViewModel() {
        entity = MBEntityType.LABEL;
=======
    private LiveData<Label> liveData;

    public LabelViewModel() {
<<<<<<< HEAD
        entity = MBEntities.LABEL;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
=======
        entity = MBEntityType.LABEL;
>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Label.class));
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
    public LiveData<Label> getData() {
        return liveData;
=======
    public LiveData<Label> initializeData() {
        return labelData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("label", MBID, Constants.LOOKUP_LABEL_PARAMS);
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
=======
    public LiveData<Label> getData() {
        return liveData;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    }

    Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update LabelData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }
}
