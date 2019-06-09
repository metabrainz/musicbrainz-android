package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.AccessToken;
import org.metabrainz.mobile.data.sources.api.entities.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("https://musicbrainz.org/oauth2/token")
    Call<AccessToken> getAccessToken(@Field("code") String code,
                                     @Field("grant_type") String grantType,
                                     @Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("redirect_uri") String redirectUri);

    @FormUrlEncoded
    @POST("https://musicbrainz.org/oauth2/token")
    Call<AccessToken> refreshAccessToken(@Field("refresh_token") String refreshToken,
                                         @Field("grant_type") String grantType,
                                         @Field("client_id") String clientId,
                                         @Field("client_secret") String clientSecret);

    @GET("https://musicbrainz.org/oauth2/userinfo")
    Call<UserInfo> getUserInfo();


}
