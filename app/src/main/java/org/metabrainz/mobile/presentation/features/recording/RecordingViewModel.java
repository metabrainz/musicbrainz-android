package org.metabrainz.mobile.presentation.features.recording;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.Resource;

public class RecordingViewModel extends LookupViewModel {

    private final LiveData<Resource<Recording>> liveData;

    @ViewModelInject
    public RecordingViewModel(LookupRepository repository) {
        super(repository);
        entity = MBEntityType.RECORDING;
        liveData = Transformations.map(jsonLiveData, RecordingViewModel::toRecording);
    }

    private static Resource<Recording> toRecording(Resource<String> data) {
        Resource<Recording> resource;
        try {
            if (data != null && data.getStatus() == Resource.Status.SUCCESS) {
                Recording recording = new Gson().fromJson(data.getData(), Recording.class);
                resource = new Resource<>(Resource.Status.SUCCESS, recording);
            } else
                resource = Resource.getFailure(Recording.class);
        } catch (Exception e) {
            e.printStackTrace();
            resource = Resource.getFailure(Recording.class);
        }
        return resource;
    }

    @Override
    public LiveData<Resource<Recording>> getData() {
        return liveData;
    }
}
