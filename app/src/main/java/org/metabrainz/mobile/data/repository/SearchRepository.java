package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.SearchService;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.data.sources.api.entities.response.search.ArtistSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.EventSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.InstrumentSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.LabelSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.RecordingSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.ReleaseGroupSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.ReleaseSearchResponse;
import org.metabrainz.mobile.util.Log;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final static SearchService service = MusicBrainzServiceGenerator
            .createService(SearchService.class, true);
    private static SearchRepository repository;
    private final MutableLiveData<List<Artist>> artistResults;
    private final MutableLiveData<List<Release>> releaseResults;
    private final MutableLiveData<List<Label>> labelResults;
    private final MutableLiveData<List<ReleaseGroup>> releaseGroupResults;
    private final MutableLiveData<List<Recording>> recordingResults;
    private final MutableLiveData<List<Instrument>> instrumentResults;
    private final MutableLiveData<List<Event>> eventResults;

    private SearchRepository() {
        artistResults = new MutableLiveData<>();
        releaseResults = new MutableLiveData<>();
        labelResults = new MutableLiveData<>();
        releaseGroupResults = new MutableLiveData<>();
        recordingResults = new MutableLiveData<>();
        instrumentResults = new MutableLiveData<>();
        eventResults = new MutableLiveData<>();
    }

    public static SearchRepository getRepository() {
        if (repository == null) repository = new SearchRepository();
        return repository;
    }

    public MutableLiveData<List<Artist>> getArtistResults(final String searchTerm) {
        fetchArtistResults(searchTerm);
        return artistResults;
    }

    public MutableLiveData<List<Release>> getReleaseResults(final String searchTerm) {
        fetchReleaseResults(searchTerm);
        return releaseResults;
    }

    public MutableLiveData<List<Label>> getLabelResults(final String searchTerm) {
        fetchLabelResults(searchTerm);
        return labelResults;
    }

    public MutableLiveData<List<ReleaseGroup>> getReleaseGroupResults(final String searchTerm) {
        fetchReleaseGroupResults(searchTerm);
        return releaseGroupResults;
    }

    public MutableLiveData<List<Recording>> getRecordingResults(final String searchTerm) {
        fetchRecordingResults(searchTerm);
        return recordingResults;
    }

    public MutableLiveData<List<Instrument>> getInstrumentResults(final String searchTerm) {
        fetchInstrumentResults(searchTerm);
        return instrumentResults;
    }

    public MutableLiveData<List<Event>> getEventResults(final String searchTerm) {
        fetchEventResults(searchTerm);
        return eventResults;
    }

    private void fetchArtistResults(final String searchTerm) {
        service.searchArtist(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<ArtistSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ArtistSearchResponse> call, @NonNull Response<ArtistSearchResponse> response) {
                        ArtistSearchResponse searchResponse = response.body();
                        artistResults.postValue(Objects.requireNonNull(searchResponse).getArtists());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArtistSearchResponse> call, @NonNull Throwable t) {
                    }
                });
    }

    private void fetchReleaseResults(final String searchTerm) {
        service.searchRelease(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<ReleaseSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReleaseSearchResponse> call, @NonNull Response<ReleaseSearchResponse> response) {
                        ReleaseSearchResponse searchResponse = response.body();
                        releaseResults.postValue(Objects.requireNonNull(searchResponse).getReleases());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReleaseSearchResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    private void fetchLabelResults(final String searchTerm) {
        service.searchLabel(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<LabelSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LabelSearchResponse> call, @NonNull Response<LabelSearchResponse> response) {
                        LabelSearchResponse labelSearchResponse = response.body();
                        labelResults.postValue(Objects.requireNonNull(labelSearchResponse).getLabels());
                        for (Label label : labelSearchResponse.getLabels())
                            Log.d(label.toString());
                    }

                    @Override
                    public void onFailure(@NonNull Call<LabelSearchResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    private void fetchReleaseGroupResults(final String searchTerm) {
        service.searchReleaseGroup(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<ReleaseGroupSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReleaseGroupSearchResponse> call, @NonNull Response<ReleaseGroupSearchResponse> response) {
                        ReleaseGroupSearchResponse releaseGroupSearchResponse = response.body();
                        releaseGroupResults.postValue(Objects.requireNonNull(releaseGroupSearchResponse).getReleaseGroups());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReleaseGroupSearchResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    private void fetchRecordingResults(final String searchTerm) {
        service.searchRecording(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<RecordingSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RecordingSearchResponse> call, @NonNull Response<RecordingSearchResponse> response) {
                        RecordingSearchResponse recordingSearchResponse = response.body();
                        recordingResults.postValue(Objects.requireNonNull(recordingSearchResponse).getRecordings());
                        for (Recording recording : recordingSearchResponse.getRecordings())
                            Log.d(recording.toString());
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecordingSearchResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    private void fetchInstrumentResults(final String searchTerm) {
        service.searchInstrument(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<InstrumentSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<InstrumentSearchResponse> call, @NonNull Response<InstrumentSearchResponse> response) {
                        InstrumentSearchResponse instrumentSearchResponse = response.body();
                        instrumentResults.postValue(Objects.requireNonNull(instrumentSearchResponse).getInstruments());
                    }

                    @Override
                    public void onFailure(@NonNull Call<InstrumentSearchResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    private void fetchEventResults(final String searchTerm) {
        service.searchEvent(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<EventSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EventSearchResponse> call, @NonNull Response<EventSearchResponse> response) {
                        EventSearchResponse eventSearchResponse = response.body();
                        eventResults.postValue(Objects.requireNonNull(eventSearchResponse).getEvents());
                    }

                    @Override
                    public void onFailure(@NonNull Call<EventSearchResponse> call, @NonNull Throwable t) {

                    }
                });
    }
}
