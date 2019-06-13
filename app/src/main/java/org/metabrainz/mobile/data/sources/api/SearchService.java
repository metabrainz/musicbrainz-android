package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.response.search.ArtistSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.EventSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.InstrumentSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.LabelSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.RecordingSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.ReleaseGroupSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.search.ReleaseSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {

    @GET("artist/")
    Call<ArtistSearchResponse> searchArtist(@Query("query") String searchTerm,
                                            @Query("limit") String limit,
                                            @Query("offset") String offset);

    @GET("release/")
    Call<ReleaseSearchResponse> searchRelease(@Query("query") String searchTerm,
                                              @Query("limit") String limit,
                                              @Query("offset") String offset);

    @GET("release-group/")
    Call<ReleaseGroupSearchResponse> searchReleaseGroup(@Query("query") String searchTerm,
                                                        @Query("limit") String limit,
                                                        @Query("offset") String offset);

    @GET("label/")
    Call<LabelSearchResponse> searchLabel(@Query("query") String searchTerm,
                                          @Query("limit") String limit,
                                          @Query("offset") String offset);

    @GET("recording/")
    Call<RecordingSearchResponse> searchRecording(@Query("query") String searchTerm,
                                                  @Query("limit") String limit,
                                                  @Query("offset") String offset);

    @GET("instrument/")
    Call<InstrumentSearchResponse> searchInstrument(@Query("query") String searchTerm,
                                                    @Query("limit") String limit,
                                                    @Query("offset") String offset);

    @GET("event/")
    Call<EventSearchResponse> searchEvent(@Query("query") String searchTerm,
                                          @Query("limit") String limit,
                                          @Query("offset") String offset);

}
