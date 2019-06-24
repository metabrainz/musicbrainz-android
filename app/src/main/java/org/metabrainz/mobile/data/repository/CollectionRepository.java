package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.CollectionService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.ArtistCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.EventCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.InstrumentCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.LabelCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.RecordingCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.ReleaseCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.ReleaseGroupCollectionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionRepository {

    private final static CollectionService service = MusicBrainzServiceGenerator
            .createService(CollectionService.class, true);
    private static CollectionRepository repository;
    private static MutableLiveData<List<Collection>> collectionData;
    private static MutableLiveData<List<Artist>> artistCollectionData;
    private static MutableLiveData<List<Release>> releaseCollectionData;
    private static MutableLiveData<List<ReleaseGroup>> releaseGroupCollectionData;
    private static MutableLiveData<List<Event>> eventCollectionData;
    private static MutableLiveData<List<Instrument>> instrumentCollectionData;
    private static MutableLiveData<List<Label>> labelCollectionData;
    private static MutableLiveData<List<Recording>> recordingCollectionData;
    private Callback<ResponseBody> listResponseCallback;

    private CollectionRepository() {
        collectionData = new MutableLiveData<>();
        artistCollectionData = new MutableLiveData<>();
        recordingCollectionData = new MutableLiveData<>();
        releaseCollectionData = new MutableLiveData<>();
        releaseGroupCollectionData = new MutableLiveData<>();
        eventCollectionData = new MutableLiveData<>();
        labelCollectionData = new MutableLiveData<>();
        instrumentCollectionData = new MutableLiveData<>();
    }

    public static CollectionRepository getRepository() {
        if (repository == null)
            repository = new CollectionRepository();
        return repository;
    }

    public static void destroyRepository() {
        repository = null;
    }

    public MutableLiveData<List<Collection>> getCollectionData() {
        return collectionData;
    }

    public MutableLiveData<List<Artist>> getArtistCollectionData() {
        return artistCollectionData;
    }

    public MutableLiveData<List<Release>> getReleaseCollectionData() {
        return releaseCollectionData;
    }

    public MutableLiveData<List<ReleaseGroup>> getReleaseGroupCollectionData() {
        return releaseGroupCollectionData;
    }

    public MutableLiveData<List<Event>> getEventCollectionData() {
        return eventCollectionData;
    }

    public MutableLiveData<List<Instrument>> getInstrumentCollectionData() {
        return instrumentCollectionData;
    }

    public MutableLiveData<List<Label>> getLabelCollectionData() {
        return labelCollectionData;
    }

    public MutableLiveData<List<Recording>> getRecordingCollectionData() {
        return recordingCollectionData;
    }

    public void fetchCollections(String editor, boolean fetchPrivate) {

        listResponseCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    List<Collection> collections = new ArrayList<>();
                    CollectionUtils.setGenericCountParameter(collections, Objects.requireNonNull(response.body()).string());
                    collectionData.setValue(collections);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        };

        if (fetchPrivate)
            service.getAllUserCollections(editor, "user-collections").enqueue(listResponseCallback);
        else
            service.getPublicUserCollections(editor).enqueue(listResponseCallback);
    }

    public void fetchArtistCollectionDetails(String id) {
        service.getArtistCollectionContents(id).enqueue(new Callback<ArtistCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArtistCollectionResponse> call, @NonNull Response<ArtistCollectionResponse> response) {
                ArtistCollectionResponse collectionResponse = response.body();
                artistCollectionData.setValue(Objects.requireNonNull(collectionResponse).getArtists());
            }

            @Override
            public void onFailure(@NonNull Call<ArtistCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchReleaseCollectionDetails(String id) {
        service.getReleaseCollectionContents(id).enqueue(new Callback<ReleaseCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReleaseCollectionResponse> call, @NonNull Response<ReleaseCollectionResponse> response) {
                ReleaseCollectionResponse collectionResponse = response.body();
                releaseCollectionData.setValue(Objects.requireNonNull(collectionResponse).getReleases());
            }

            @Override
            public void onFailure(@NonNull Call<ReleaseCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchReleaseGroupCollectionDetails(String id) {
        service.getReleaseGroupCollectionContents(id).enqueue(new Callback<ReleaseGroupCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReleaseGroupCollectionResponse> call, @NonNull Response<ReleaseGroupCollectionResponse> response) {
                ReleaseGroupCollectionResponse collectionResponse = response.body();
                releaseGroupCollectionData.setValue(Objects.requireNonNull(collectionResponse).getReleaseGroups());
            }

            @Override
            public void onFailure(@NonNull Call<ReleaseGroupCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchLabelCollectionDetails(String id) {
        service.getLabelCollectionContents(id).enqueue(new Callback<LabelCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<LabelCollectionResponse> call, @NonNull Response<LabelCollectionResponse> response) {
                LabelCollectionResponse collectionResponse = response.body();
                labelCollectionData.setValue(Objects.requireNonNull(collectionResponse).getLabels());
            }

            @Override
            public void onFailure(@NonNull Call<LabelCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchEventCollectionDetails(String id) {
        service.getEventCollectionContents(id).enqueue(new Callback<EventCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<EventCollectionResponse> call, @NonNull Response<EventCollectionResponse> response) {
                EventCollectionResponse collectionResponse = response.body();
                eventCollectionData.setValue(Objects.requireNonNull(collectionResponse).getEvents());
            }

            @Override
            public void onFailure(@NonNull Call<EventCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchInstrumentCollectionDetails(String id) {
        service.getInstrumentCollectionContents(id).enqueue(new Callback<InstrumentCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstrumentCollectionResponse> call, @NonNull Response<InstrumentCollectionResponse> response) {
                InstrumentCollectionResponse collectionResponse = response.body();
                instrumentCollectionData.setValue(Objects.requireNonNull(collectionResponse).getInstruments());
            }

            @Override
            public void onFailure(@NonNull Call<InstrumentCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchRecordingCollectionDetails(String id) {
        service.getRecordingCollectionContents(id).enqueue(new Callback<RecordingCollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecordingCollectionResponse> call, @NonNull Response<RecordingCollectionResponse> response) {
                RecordingCollectionResponse collectionResponse = response.body();
                recordingCollectionData.setValue(Objects.requireNonNull(collectionResponse).getRecordings());
            }

            @Override
            public void onFailure(@NonNull Call<RecordingCollectionResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
