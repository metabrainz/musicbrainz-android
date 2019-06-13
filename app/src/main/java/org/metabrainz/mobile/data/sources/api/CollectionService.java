package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.response.collection.ArtistCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.EventCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.InstrumentCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.LabelCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.RecordingCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.ReleaseCollectionResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.collection.ReleaseGroupCollectionResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CollectionService {

    @GET("collection")
    Call<ResponseBody> getPublicUserCollections(@Query("editor") String name);

    @GET("collection")
    Call<ResponseBody> getAllUserCollections(@Query("editor") String name,
                                             @Query("inc") String params);

    @GET("artist")
    Call<ArtistCollectionResponse> getArtistCollectionContents(@Query("collection") String id);

    @GET("release")
    Call<ReleaseCollectionResponse> getReleaseCollectionContents(@Query("collection") String id);

    @GET("release-group")
    Call<ReleaseGroupCollectionResponse> getReleaseGroupCollectionContents(@Query("collection") String id);

    @GET("label")
    Call<LabelCollectionResponse> getLabelCollectionContents(@Query("collection") String id);

    @GET("event")
    Call<EventCollectionResponse> getEventCollectionContents(@Query("collection") String id);

    @GET("instrument")
    Call<InstrumentCollectionResponse> getInstrumentCollectionContents(@Query("collection") String id);

    @GET("recording")
    Call<RecordingCollectionResponse> getRecordingCollectionContents(@Query("collection") String id);

}
