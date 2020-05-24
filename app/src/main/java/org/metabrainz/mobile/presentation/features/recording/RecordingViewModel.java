package org.metabrainz.mobile.presentation.features.recording;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

<<<<<<< HEAD
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
=======
import org.metabrainz.mobile.data.sources.Constants;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class RecordingViewModel extends LookupViewModel {

<<<<<<< HEAD
    private LiveData<Recording> liveData;

    public RecordingViewModel() {
        entity = MBEntityType.RECORDING;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Recording.class));
    }

    @Override
    public LiveData<Recording> getData() {
        return liveData;
=======
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
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
    }
}
