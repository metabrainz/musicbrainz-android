package org.metabrainz.mobile.presentation.features.tagger;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.TaggerRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.List;

public class TaggerViewModel extends ViewModel {

    private MutableLiveData<List<Recording>> recordingResponseData;
    private MutableLiveData<Release> matchedReleaseData;
    private TaggerRepository repository = TaggerRepository.getRepository();

    public TaggerViewModel() {
    }

    public MutableLiveData<List<Recording>> getRecordingData() {
        if (recordingResponseData == null) recordingResponseData = repository.getRecordingData();
        return recordingResponseData;
    }

    public MutableLiveData<Release> getMatchedReleaseData() {
        if (matchedReleaseData == null) matchedReleaseData = repository.getMatchedReleaseData();
        return matchedReleaseData;
    }

    public void fetchRecordings(String query) {
        repository.fetchRecordings(query);
    }

    public void fetchMatchedRelease(String MBID) {
        repository.fetchMatchedRelease(MBID);
    }

    public void fetchRecordingsWithFingerprint(long duration, String fingerprint) {
        repository.fetchAcoustIDResults(duration, fingerprint);
    }

    @Override
    protected void onCleared() {
        repository.destroyRepository();
        super.onCleared();
    }
}
