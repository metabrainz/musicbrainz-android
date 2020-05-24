package org.metabrainz.mobile.data.sources.api;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.response.BarcodeReleaseResponse;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LookupService {

    @GET("{entity}/{MBID}")
    Call<ResponseBody> lookupEntityData(@Path("entity") String entity,
                                        @Path("MBID") String MBID,
                                        @Query("inc") String params);

    @GET("http://coverartarchive.org/release/{MBID}")
    Single<CoverArt> getCoverArt(@Path("MBID") String MBID);

    @GET("https://en.wikipedia.org/api/rest_v1/page/summary/{title}")
    Call<WikiSummary> getWikipediaSummary(@Path("title") String title);

    @GET("https://www.wikidata.org/w/api.php" +
            "?action=wbgetentities&format=xml&props=sitelinks/urls&format=json")
    Call<ResponseBody> getWikipediaLink(@Query("ids") String id);

    @GET("http://coverartarchive.org/release/{MBID}")
    Call<CoverArt> getCoverArtAll(@Path("MBID") String MBID);

    @GET("release/")
    Call<BarcodeReleaseResponse> lookupReleaseWithBarcode(@Query("query") String barcode);
}
