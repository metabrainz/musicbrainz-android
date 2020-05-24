package org.metabrainz.mobile.data.sources.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchService {

    @GET("{entity}/")
    Call<ResponseBody> searchEntity(@Path("entity") String entity,
                                    @Query("query") String searchTerm,
                                    @Query("limit") String limit,
                                    @Query("offset") String offset);

}
