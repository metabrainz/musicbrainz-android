package org.metabrainz.mobile.presentation.features.recording;

import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class RecordingViewModel extends LookupViewModel {

    public RecordingViewModel() {
        entity = MBEntities.RECORDING;
        liveData = Transformations.map(repository.initializeData(),
                data -> new Gson().fromJson(data, Recording.class));
    }

}
