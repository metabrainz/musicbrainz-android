package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.response.ArtistSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.EventSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.InstrumentSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.LabelSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.RecordingSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.ReleaseGroupSearchResponse;
import org.metabrainz.mobile.data.sources.api.entities.response.ReleaseSearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SearchService {

    @FormUrlEncoded
    @POST("https://test.musicbrainz.org/oauth2/token")
    Call<AccessToken> getAccessToken(@Field("code") String code,
                                     @Field("grant_type") String grantType,
                                     @Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("redirect_uri") String redirectUri);

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
