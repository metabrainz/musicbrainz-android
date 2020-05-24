package org.metabrainz.mobile.presentation.features.recording;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class RecordingViewModel extends LookupViewModel {

    private LiveData<Recording> recordingData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, Recording.class));

    public RecordingViewModel() {
    }

    @Override
    public LiveData<Recording> initializeData() {
        return recordingData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("recording", MBID, Constants.LOOKUP_RECORDING_PARAMS);
    }
}
