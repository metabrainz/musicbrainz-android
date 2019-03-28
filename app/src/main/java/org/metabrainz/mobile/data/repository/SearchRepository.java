package org.metabrainz.mobile.data.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.data.sources.api.entities.response.ArtistSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.EventSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.InstrumentSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.LabelSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.RecordingSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.ReleaseGroupSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.ReleaseSearchResponse;
import org.metabrainz.mobile.api.webservice.Constants;
import org.metabrainz.mobile.data.sources.api.SearchService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final static SearchService service = MusicBrainzServiceGenerator
            .createService(SearchService.class);
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
                    public void onResponse(Call<ArtistSearchResponse> call, Response<ArtistSearchResponse> response) {
                        ArtistSearchResponse searchResponse = response.body();
                        artistResults.postValue(searchResponse.getArtists());
                    }

                    @Override
                    public void onFailure(Call<ArtistSearchResponse> call, Throwable t) {
                    }
                });
    }

    private void fetchReleaseResults(final String searchTerm) {
        service.searchRelease(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<ReleaseSearchResponse>() {
                    @Override
                    public void onResponse(Call<ReleaseSearchResponse> call, Response<ReleaseSearchResponse> response) {
                        ReleaseSearchResponse searchResponse = response.body();
                        releaseResults.postValue(searchResponse.getReleases());
                    }

                    @Override
                    public void onFailure(Call<ReleaseSearchResponse> call, Throwable t) {

                    }
                });
    }

    private void fetchLabelResults(final String searchTerm) {
        service.searchLabel(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<LabelSearchResponse>() {
                    @Override
                    public void onResponse(Call<LabelSearchResponse> call, Response<LabelSearchResponse> response) {
                        LabelSearchResponse labelSearchResponse = response.body();
                        labelResults.postValue(labelSearchResponse.getLabels());
                        for (Label label : labelSearchResponse.getLabels())
                            Log.d(label.toString());
                    }

                    @Override
                    public void onFailure(Call<LabelSearchResponse> call, Throwable t) {

                    }
                });
    }

    private void fetchReleaseGroupResults(final String searchTerm) {
        service.searchReleaseGroup(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<ReleaseGroupSearchResponse>() {
                    @Override
                    public void onResponse(Call<ReleaseGroupSearchResponse> call, Response<ReleaseGroupSearchResponse> response) {
                        ReleaseGroupSearchResponse releaseGroupSearchResponse = response.body();
                        releaseGroupResults.postValue(releaseGroupSearchResponse.getReleaseGroups());
                    }

                    @Override
                    public void onFailure(Call<ReleaseGroupSearchResponse> call, Throwable t) {

                    }
                });
    }

    private void fetchRecordingResults(final String searchTerm) {
        service.searchRecording(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<RecordingSearchResponse>() {
                    @Override
                    public void onResponse(Call<RecordingSearchResponse> call, Response<RecordingSearchResponse> response) {
                        RecordingSearchResponse recordingSearchResponse = response.body();
                        recordingResults.postValue(recordingSearchResponse.getRecordings());
                        for (Recording recording : recordingSearchResponse.getRecordings())
                            Log.d(recording.toString());
                    }

                    @Override
                    public void onFailure(Call<RecordingSearchResponse> call, Throwable t) {

                    }
                });
    }

    private void fetchInstrumentResults(final String searchTerm) {
        service.searchInstrument(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<InstrumentSearchResponse>() {
                    @Override
                    public void onResponse(Call<InstrumentSearchResponse> call, Response<InstrumentSearchResponse> response) {
                        InstrumentSearchResponse instrumentSearchResponse = response.body();
                        instrumentResults.postValue(instrumentSearchResponse.getInstruments());
                    }

                    @Override
                    public void onFailure(Call<InstrumentSearchResponse> call, Throwable t) {

                    }
                });
    }

    private void fetchEventResults(final String searchTerm) {
        service.searchEvent(searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<EventSearchResponse>() {
                    @Override
                    public void onResponse(Call<EventSearchResponse> call, Response<EventSearchResponse> response) {
                        EventSearchResponse eventSearchResponse = response.body();
                        eventResults.postValue(eventSearchResponse.getEvents());
                    }

                    @Override
                    public void onFailure(Call<EventSearchResponse> call, Throwable t) {

                    }
                });
    }
}
