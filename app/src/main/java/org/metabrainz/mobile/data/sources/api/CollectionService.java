package org.metabrainz.mobile.data.sources.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CollectionService {

    @GET("collection")
    Call<ResponseBody> getPublicUserCollections(@Query("editor") String name);

    @GET("collection")
    Call<ResponseBody> getAllUserCollections(@Query("editor") String name,
                                             @Query("inc") String params);

    @GET("{entity}")
    Call<ResponseBody> getCollectionContents(@Path("entity") String entity,
                                             @Query("collection") String id);

}
