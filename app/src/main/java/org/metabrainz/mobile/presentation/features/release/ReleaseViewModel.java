package org.metabrainz.mobile.presentation.features.release;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class ReleaseViewModel extends LookupViewModel {

    private final LiveData<CoverArt> coverArtData;
    private final LiveData<Release> liveData;

    public ReleaseViewModel() {
        entity = MBEntityType.RELEASE;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Release.class));
        coverArtData = Transformations.switchMap(MBID, id -> repository.fetchCoverArt(id));
    }

    @Override
    public LiveData<Release> getData() {
        return liveData;
    }

    LiveData<CoverArt> fetchCoverArt() {
        return coverArtData;
    }

}
