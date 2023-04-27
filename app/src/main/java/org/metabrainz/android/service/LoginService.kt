package org.metabrainz.android.service

import org.metabrainz.android.model.entities.AccessToken
import org.metabrainz.android.model.userdata.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    @FormUrlEncoded
    @POST
    fun getAccessToken(@Url url: String?,
                       @Field("code") code: String?,
                       @Field("grant_type") grantType: String?,
                       @Field("client_id") clientId: String?,
                       @Field("client_secret") clientSecret: String?,
                       @Field("redirect_uri") redirectUri: String?): Call<org.metabrainz.android.model.entities.AccessToken?>?

    @FormUrlEncoded
    @POST("https://musicbrainz.org/oauth2/token")
    fun refreshAccessToken(@Field("refresh_token") refreshToken: String?,
                           @Field("grant_type") grantType: String?,
                           @Field("client_id") clientId: String?,
                           @Field("client_secret") clientSecret: String?): Call<org.metabrainz.android.model.entities.AccessToken?>?

    @get:GET("https://musicbrainz.org/oauth2/userinfo")
    val userInfo: Call<UserInfo?>?
}