package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiDataResponse;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.Objects;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookupRepository {

    public static final int METHOD_WIKIPEDIA_URL = 0;
    public static final int METHOD_WIKIDATA_ID = 1;

    private final static LookupService service =
            MusicBrainzServiceGenerator.createService(LookupService.class, true);
    private static LookupRepository repository;


    private LookupRepository() {
    }

    public static LookupRepository getRepository() {
        if (repository == null) repository = new LookupRepository();
        return repository;
    }

    public static void destroyRepository() {
        repository = null;
    }

    public LiveData<String> fetchData(String entity, String MBID, String params) {
        MutableLiveData<String> entityLiveData = new MutableLiveData<>();
        service.lookupEntityData(entity, MBID, params)
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                try {
                    entityLiveData.setValue(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
                });
        return entityLiveData;
    }

    public SingleLiveEvent<WikiSummary> fetchWikiSummary(String string, int method) {
        SingleLiveEvent<WikiSummary> wikiSummary = new SingleLiveEvent<>();
        if (method == METHOD_WIKIPEDIA_URL)
            fetchWiki(string, wikiSummary);
        else
            fetchWikiData(string, wikiSummary);
        return wikiSummary;
    }

    private void fetchWiki(String title, SingleLiveEvent<WikiSummary> wikiSummary) {
        service.getWikipediaSummary(title).enqueue(new Callback<WikiSummary>() {
            @Override
            public void onResponse(@NonNull Call<WikiSummary> call, @NonNull Response<WikiSummary> response) {
                wikiSummary.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<WikiSummary> call, @NonNull Throwable t) {

            }
        });
    }

    private void fetchWikiData(String id, SingleLiveEvent<WikiSummary> wikiSummary) {
        service.getWikipediaLink(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JsonElement element = new JsonParser().parse(jsonResponse);
                    JsonObject result = element.getAsJsonObject()
                            .getAsJsonObject("entities").getAsJsonObject(id);
                    WikiDataResponse wikiDataResponse = new Gson().fromJson(result, WikiDataResponse.class);
                    String title = Objects.requireNonNull(wikiDataResponse.getSitelinks().get("enwiki")).getTitle();
                    fetchWiki(title, wikiSummary);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }

    public LiveData<CoverArt> fetchCoverArt(String MBID) {
        MutableLiveData<CoverArt> coverArtData = new MutableLiveData<>();
        service.getCoverArtAll(MBID).enqueue(new Callback<CoverArt>() {
            @Override
            public void onResponse(@NonNull Call<CoverArt> call, @NonNull Response<CoverArt> response) {
                coverArtData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CoverArt> call, @NonNull Throwable t) {

            }
        });
        return coverArtData;
    }

    /**
     * For a given release ID, fetches the cover arts and updates the release w¡th that info
     *
     * @param release Release for which the cover art is to be retrieved
     */
    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        return service.getCoverArt(release.getMbid());
    }
}
