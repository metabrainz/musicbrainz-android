package org.metabrainz.mobile.data.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.api.webservice.Constants;
import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordingLookupRepository {
    private final static LookupService service = MusicBrainzServiceGenerator
            .createService(LookupService.class, true);
    private static RecordingLookupRepository repository;
    private static MutableLiveData<Recording> recordingData;

    private RecordingLookupRepository() {

        recordingData = new MutableLiveData<>();
    }

    public static RecordingLookupRepository getRepository() {
        if (repository == null) repository = new RecordingLookupRepository();
        return repository;
    }

    public static void destroyRepository() {
        repository = null;
    }

    public MutableLiveData<Recording> initializeRecordingData() {
        return recordingData;
    }

    public void getRecording(String MBID) {
        service.lookupRecording(MBID, Constants.LOOKUP_RECORDING_PARAMS).enqueue(new Callback<Recording>() {
            @Override
            public void onResponse(Call<Recording> call, Response<Recording> response) {
                Recording Recording = response.body();
                recordingData.setValue(Recording);
            }

            @Override
            public void onFailure(Call<Recording> call, Throwable t) {

            }
        });
    }
}
