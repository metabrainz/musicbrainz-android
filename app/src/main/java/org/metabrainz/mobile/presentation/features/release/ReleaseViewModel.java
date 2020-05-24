package org.metabrainz.mobile.presentation.features.release;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class ReleaseViewModel extends LookupViewModel {

    private LiveData<Release> releaseData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, Release.class));
    private MutableLiveData<CoverArt> coverArtData;

    public ReleaseViewModel() {
    }

    @Override
    public LiveData<Release> initializeData() {
        return releaseData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("release", MBID, Constants.LOOKUP_RELEASE_PARAMS);
    }

    public MutableLiveData<CoverArt> initializeCoverArtData() {
        if (coverArtData == null)
            coverArtData = repository.initializeCoverArtData();
        return coverArtData;
    }

    public void getCoverArtData() {
        repository.getCoverArt(MBID);
    }

}
