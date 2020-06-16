package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.ListenSubmitBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ListenSubmitService {

    @POST("https://api.listenbrainz.org/1/submit-listens")
    Call<ResponseBody> submitListen(@Header("Authorization") String token,
                                    @Body ListenSubmitBody body);
}
