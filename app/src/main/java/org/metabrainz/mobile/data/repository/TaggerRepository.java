package org.metabrainz.mobile.data.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.TaggerService;
import org.metabrainz.mobile.data.sources.api.entities.acoustid.AcoustIDResponse;
import org.metabrainz.mobile.data.sources.api.entities.acoustid.Result;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.response.search.RecordingSearchResponse;
import org.metabrainz.mobile.util.TaggerUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator.ACOUST_ID_KEY;

public class TaggerRepository {
    private static final TaggerService service = MusicBrainzServiceGenerator
            .createService(TaggerService.class, false);

    private static TaggerRepository repository;
    private final MutableLiveData<List<Recording>> recordingResponseData;
    private final MutableLiveData<Release> matchedReleaseData;

    private TaggerRepository() {
        recordingResponseData = new MutableLiveData<>();
        matchedReleaseData = new MutableLiveData<>();
    }

    public static TaggerRepository getRepository() {
        if (repository == null) repository = new TaggerRepository();
        return repository;
    }

    public MutableLiveData<List<Recording>> getRecordingData() {
        return recordingResponseData;
    }

    public void fetchRecordings(String query) {
        service.searchRecording(query, Constants.LIMIT).enqueue(new Callback<RecordingSearchResponse>() {
            @Override
            public void onResponse(Call<RecordingSearchResponse> call, Response<RecordingSearchResponse> response) {
                RecordingSearchResponse data = response.body();
                recordingResponseData.setValue(data.getRecordings());
            }

            @Override
            public void onFailure(Call<RecordingSearchResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<Release> getMatchedReleaseData() {
        return matchedReleaseData;
    }

    public void fetchMatchedRelease(String MBID) {
        service.lookupRecording(MBID, Constants.TAGGER_RELEASE_PARAMS)
                .enqueue(new Callback<Release>() {
                    @Override
                    public void onResponse(Call<Release> call, Response<Release> response) {
                        Release release = response.body();
                        matchedReleaseData.setValue(release);
                    }

                    @Override
                    public void onFailure(Call<Release> call, Throwable t) {

                    }
                });
    }

    public void fetchAcoustIDResults(long duration, String fingerprint) {
        service.lookupFingerprint(ACOUST_ID_KEY, Constants.ACOUST_ID_RESPONSE_PARAMS, duration, fingerprint)
                .enqueue(new Callback<AcoustIDResponse>() {
                    @Override
                    public void onResponse(Call<AcoustIDResponse> call, Response<AcoustIDResponse> response) {
                        List<Result> result = response.body().getResults();
                        recordingResponseData.setValue(TaggerUtils.parseResults(result));
                    }

                    @Override
                    public void onFailure(Call<AcoustIDResponse> call, Throwable t) {

                    }
                });
    }

    public void destroyRepository() {
        repository = null;
    }
}
