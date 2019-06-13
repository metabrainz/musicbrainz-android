package org.metabrainz.mobile.presentation.features.collection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.CollectionRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.List;

public class CollectionViewModel extends ViewModel {

    private static CollectionRepository repository = CollectionRepository.getRepository();
    private MutableLiveData<List<Collection>> collectionData;
    private MutableLiveData<List<Artist>> artistCollectionData;
    private MutableLiveData<List<Release>> releaseCollectionData;
    private MutableLiveData<List<ReleaseGroup>> releaseGroupCollectionData;
    private MutableLiveData<List<Event>> eventCollectionData;
    private MutableLiveData<List<Instrument>> instrumentCollectionData;
    private MutableLiveData<List<Label>> labelCollectionData;
    private MutableLiveData<List<Recording>> recordingCollectionData;

    public CollectionViewModel() {
    }

    public MutableLiveData<List<Collection>> getCollectionData() {
        if (collectionData == null)
            collectionData = repository.getCollectionData();
        return collectionData;
    }

    public MutableLiveData<List<Artist>> getArtistCollectionData() {
        if (artistCollectionData == null)
            artistCollectionData = repository.getArtistCollectionData();
        return artistCollectionData;
    }

    public MutableLiveData<List<Release>> getReleaseCollectionData() {
        if (releaseCollectionData == null)
            releaseCollectionData = repository.getReleaseCollectionData();
        return releaseCollectionData;
    }

    public MutableLiveData<List<ReleaseGroup>> getReleaseGroupCollectionData() {
        if (releaseGroupCollectionData == null)
            releaseGroupCollectionData = repository.getReleaseGroupCollectionData();
        return releaseGroupCollectionData;
    }

    public MutableLiveData<List<Event>> getEventCollectionData() {
        if (eventCollectionData == null)
            eventCollectionData = repository.getEventCollectionData();
        return eventCollectionData;
    }

    public MutableLiveData<List<Instrument>> getInstrumentCollectionData() {
        if (instrumentCollectionData == null)
            instrumentCollectionData = repository.getInstrumentCollectionData();
        return instrumentCollectionData;
    }

    public MutableLiveData<List<Label>> getLabelCollectionData() {
        if (labelCollectionData == null)
            labelCollectionData = repository.getLabelCollectionData();
        return labelCollectionData;
    }

    public MutableLiveData<List<Recording>> getRecordingCollectionData() {
        if (recordingCollectionData == null)
            recordingCollectionData = repository.getRecordingCollectionData();
        return recordingCollectionData;
    }

    public void fetchCollections(String editor, boolean fetchPrivate) {
        repository.fetchCollections(editor, fetchPrivate);
    }

    public void fetchArtistCollectionDetails(String id) {
        repository.fetchArtistCollectionDetails(id);
    }

    public void fetchReleaseCollectionDetails(String id) {
        repository.fetchReleaseCollectionDetails(id);
    }

    public void fetchReleaseGroupCollectionDetails(String id) {
        repository.fetchReleaseGroupCollectionDetails(id);
    }

    public void fetchLabelCollectionDetails(String id) {
        repository.fetchLabelCollectionDetails(id);
    }

    public void fetchEventCollectionDetails(String id) {
        repository.fetchEventCollectionDetails(id);
    }

    public void fetchInstrumentCollectionDetails(String id) {
        repository.fetchInstrumentCollectionDetails(id);
    }

    public void fetchRecordingCollectionDetails(String id) {
        repository.fetchRecordingCollectionDetails(id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionRepository.destroyRepository();
    }
}
