package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.TaggerService;
import org.metabrainz.mobile.data.sources.api.entities.acoustid.AcoustIDResponse;
import org.metabrainz.mobile.data.sources.api.entities.acoustid.Result;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.response.RecordingSearchResponse;
import org.metabrainz.mobile.util.TaggerUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator.ACOUST_ID_KEY;

@Singleton
public class TaggerRepository {

    private final TaggerService service;

    @Inject
    public TaggerRepository(TaggerService service) {
        this.service = service;
    }

    public LiveData<List<Recording>> fetchRecordings(String query) {
        MutableLiveData<List<Recording>> recordingResponseData = new MutableLiveData<>();
        service.searchRecording(query, Constants.LIMIT).enqueue(new Callback<RecordingSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecordingSearchResponse> call, @NonNull Response<RecordingSearchResponse> response) {
                try {
                    RecordingSearchResponse data = response.body();
                    recordingResponseData.setValue(data.getRecordings());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecordingSearchResponse> call, @NonNull Throwable t) {}
        });
        return recordingResponseData;
    }


    public LiveData<Release> fetchMatchedRelease(String MBID) {
        MutableLiveData<Release> matchedReleaseData = new MutableLiveData<>();
        service.lookupRecording(MBID, Constants.TAGGER_RELEASE_PARAMS).enqueue(new Callback<Release>() {
            @Override
            public void onResponse(@NonNull Call<Release> call, @NonNull Response<Release> response) {
                try {
                    Release release = response.body();
                    matchedReleaseData.setValue(release);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Release> call, @NonNull Throwable t) {}
        });
        return matchedReleaseData;
    }

    public LiveData<List<Recording>> fetchAcoustIDResults(long duration, String fingerprint) {
        MutableLiveData<List<Recording>> recordingResponseData = new MutableLiveData<>();
        service.lookupFingerprint(ACOUST_ID_KEY, Constants.ACOUST_ID_RESPONSE_PARAMS, duration, fingerprint)
            .enqueue(new Callback<AcoustIDResponse>() {
                @Override
                public void onResponse(@NonNull Call<AcoustIDResponse> call, @NonNull Response<AcoustIDResponse> response) {
                    try{
                        List<Result> result = response.body().getResults();
                        recordingResponseData.setValue(TaggerUtils.parseResults(result));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AcoustIDResponse> call, @NonNull Throwable t) {}
            });
        return recordingResponseData;
    }

}
