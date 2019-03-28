package org.metabrainz.mobile.presentation.features.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.data.sources.repository.SearchRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private static SearchRepository repository = SearchRepository.getRepository();
    private MutableLiveData<List<Artist>> artistSearchResponse;
    private MutableLiveData<List<Release>> releaseSearchResponse;
    private MutableLiveData<List<Recording>> recordingResponse;
    private MutableLiveData<List<Label>> labelResponse;
    private MutableLiveData<List<ReleaseGroup>> releaseGroupResponse;
    private MutableLiveData<List<Event>> eventResponse;
    private MutableLiveData<List<Instrument>> instrumentResponse;
    private String searchQuery;
    private String searchEntity;
    public boolean queryHasChanged = true;

    public SearchViewModel() {
    }

    public void setSearchQuery(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty() &&
                (searchQuery == null || searchQuery.isEmpty() || !searchQuery.equals(searchTerm))) {
            searchQuery = searchTerm;
            queryHasChanged = true;
        } else queryHasChanged = false;
    }

    public void prepareSearch(String searchTerm, String entity) {
        setSearchQuery(searchTerm);
        if (entity != null && !entity.isEmpty())
            searchEntity = entity;
    }

    public MutableLiveData<List<Artist>> getArtistSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (artistSearchResponse == null || queryHasChanged) {
            artistSearchResponse = loadArtistSearchResponse(searchQuery);
        }
        return artistSearchResponse;
    }

    public MutableLiveData<List<Release>> getReleaseSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (releaseSearchResponse == null || queryHasChanged) {
            releaseSearchResponse = loadReleaseSearchResponse(searchQuery);
        }
        return releaseSearchResponse;
    }

    public MutableLiveData<List<Label>> getLabelSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (labelResponse == null || queryHasChanged) {
            labelResponse = loadLabelSearchResponse(searchQuery);
        }
        return labelResponse;
    }

    public MutableLiveData<List<Recording>> getRecordingSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (recordingResponse == null || queryHasChanged) {
            recordingResponse = loadRecordingSearchResponse(searchQuery);
        }
        return recordingResponse;
    }

    public MutableLiveData<List<ReleaseGroup>> getReleaseGroupSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (releaseGroupResponse == null || queryHasChanged) {
            releaseGroupResponse = loadReleaseGroupSearchResponse(searchQuery);
        }
        return releaseGroupResponse;
    }

    public MutableLiveData<List<Event>> getEventSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (eventResponse == null || queryHasChanged) {
            eventResponse = loadEventSearchResponse(searchQuery);
        }
        return eventResponse;
    }

    public MutableLiveData<List<Instrument>> getInstrumentSearchResponse(String searchTerm) {
        setSearchQuery(searchTerm);
        if (instrumentResponse == null || queryHasChanged) {
            instrumentResponse = loadInstrumentSearchResponse(searchQuery);
        }
        return instrumentResponse;
    }

    private MutableLiveData<List<Artist>> loadArtistSearchResponse(String searchTerm) {
        return repository.getArtistResults(searchTerm);
    }

    private MutableLiveData<List<Release>> loadReleaseSearchResponse(String searchTerm) {
        return repository.getReleaseResults(searchTerm);
    }

    private MutableLiveData<List<Label>> loadLabelSearchResponse(String searchTerm) {
        return repository.getLabelResults(searchTerm);
    }

    private MutableLiveData<List<Recording>> loadRecordingSearchResponse(String searchTerm) {
        return repository.getRecordingResults(searchTerm);
    }

    private MutableLiveData<List<ReleaseGroup>> loadReleaseGroupSearchResponse(String searchTerm) {
        return repository.getReleaseGroupResults(searchTerm);
    }

    private MutableLiveData<List<Event>> loadEventSearchResponse(String searchTerm) {
        return repository.getEventResults(searchTerm);
    }

    private MutableLiveData<List<Instrument>> loadInstrumentSearchResponse(String searchTerm) {
        return repository.getInstrumentResults(searchTerm);
    }
}
