package org.metabrainz.mobile.presentation.features.release;

import androidx.lifecycle.LiveData;
<<<<<<< HEAD
<<<<<<< HEAD
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

=======
import androidx.lifecycle.MutableLiveData;
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.Constants;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class ReleaseViewModel extends LookupViewModel {

<<<<<<< HEAD
<<<<<<< HEAD
    private LiveData<CoverArt> coverArtData;
    private LiveData<Release> liveData;
=======
    private LiveData<Release> releaseData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, Release.class));
    private MutableLiveData<CoverArt> coverArtData;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

    public ReleaseViewModel() {
        entity = MBEntityType.RELEASE;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Release.class));
        coverArtData = Transformations.switchMap(MBID, id -> repository.fetchCoverArt(id));
    }

    @Override
<<<<<<< HEAD
    public LiveData<Release> getData() {
        return liveData;
=======
    public LiveData<Release> initializeData() {
        return releaseData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("release", MBID, Constants.LOOKUP_RELEASE_PARAMS);
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
    }

    LiveData<CoverArt> fetchCoverArt() {
        return coverArtData;
    }

<<<<<<< HEAD
=======
    public void getCoverArtData() {
        repository.getCoverArt(MBID);
=======
    private LiveData<CoverArt> coverArtData;
    private LiveData<Release> liveData;

    public ReleaseViewModel() {
        entity = MBEntities.RELEASE;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Release.class));
        coverArtData = Transformations.switchMap(MBID, id -> repository.fetchCoverArt(id));
    }

    @Override
    public LiveData<Release> getData() {
        return liveData;
    }

    LiveData<CoverArt> fetchCoverArt() {
        return coverArtData;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    }

>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
}
