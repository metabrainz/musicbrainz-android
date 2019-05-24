package org.metabrainz.mobile.presentation.features.recording;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.RecordingLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;

public class RecordingViewModel extends ViewModel {

    private RecordingLookupRepository repository = RecordingLookupRepository.getRepository();
    private MutableLiveData<Recording> recordingData;
    private String MBID;

    public RecordingViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public MutableLiveData<Recording> initializeRecordingData() {
        // Obtain live data from the repository if not already present
        if (recordingData == null)
            recordingData = repository.initializeRecordingData();
        return recordingData;
    }

    public void getRecordingData() {
        // Call the repository to query the database to update the recording data
        repository.getRecording(MBID);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        RecordingLookupRepository.destroyRepository();
    }
}
