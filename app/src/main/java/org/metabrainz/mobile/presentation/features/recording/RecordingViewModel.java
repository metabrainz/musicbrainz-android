package org.metabrainz.mobile.presentation.features.recording;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class RecordingViewModel extends LookupViewModel {

    private final LiveData<Recording> liveData;

    public RecordingViewModel() {
        entity = MBEntityType.RECORDING;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Recording.class));
    }

    @Override
    public LiveData<Recording> getData() {
        return liveData;
    }
}
