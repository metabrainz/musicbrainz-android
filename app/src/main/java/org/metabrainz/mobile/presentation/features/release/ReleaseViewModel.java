package org.metabrainz.mobile.presentation.features.release;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class ReleaseViewModel extends LookupViewModel {

    private MutableLiveData<CoverArt> coverArtData;

    public ReleaseViewModel() {
        entity = MBEntities.RELEASE;
        liveData = Transformations.map(repository.initializeData(),
                data -> new Gson().fromJson(data, Release.class));
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
