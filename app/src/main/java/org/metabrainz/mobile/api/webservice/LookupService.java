package org.metabrainz.mobile.api.webservice;

import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.obsolete.Label;
import org.metabrainz.mobile.api.data.obsolete.Recording;
import org.metabrainz.mobile.api.data.obsolete.Release;
import org.metabrainz.mobile.api.data.obsolete.ReleaseGroup;
import org.metabrainz.mobile.api.data.CoverArt;
import org.metabrainz.mobile.api.data.search.entity.Artist;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LookupService {

    @GET("artist/{MBID}")
    Call<Artist> lookupArtist(@Path("MBID") String MBID,
                              @Query("inc") String params);

    @GET("http://coverartarchive.org/release/{MBID}")
    Single<CoverArt> getCoverArt(@Path("MBID") String MBID);

    @GET("https://en.wikipedia.org/api/rest_v1/page/summary/{title}")
    Call<ArtistWikiSummary> getWikipediaSummary(@Path("title") String title);

    @GET("https://www.wikidata.org/w/api.php" +
            "?action=wbgetentities&format=xml&props=sitelinks/urls&format=json")
    Call<ResponseBody> getWikipediaLink(@Query("ids") String id);

    @GET("release/{MBID}")
    Call<Release> lookupRelease(@Path("MBID") String MBID,
                                @Query("inc") String params);
    @GET("label/{MBID}")
    Call<Label> lookupLabel(@Path("MBID") String MBID,
                            @Query("inc") String params);
    @GET("recording/{MBID}")
    Call<Recording> lookupRecording(@Path("MBID") String MBID,
                                    @Query("inc") String params);
    @GET("release/{MBID}")
    Call<ReleaseGroup> lookupReleaseGroup(@Path("MBID") String MBID,
                                          @Query("inc") String params);

    // TODO: Implement Barcode LookUp
    Release lookupReleaseUsingBarcode(String barcode) throws IOException;
}
