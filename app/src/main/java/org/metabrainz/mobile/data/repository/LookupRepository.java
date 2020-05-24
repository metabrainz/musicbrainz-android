package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
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

import java.io.IOException;
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

    private final MutableLiveData<String> entityLiveData;
    private final Callback<ResponseBody> lookupCallback;
    private final SingleLiveEvent<WikiSummary> wikiSummary;
    private final MutableLiveData<CoverArt> coverArtData;

    private LookupRepository() {
        entityLiveData = new MutableLiveData<>();
        wikiSummary = new SingleLiveEvent<>();
        coverArtData = new MutableLiveData<>();
        lookupCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                try {
                    entityLiveData.setValue(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        };
    }

    public static LookupRepository getRepository() {
        if (repository == null) repository = new LookupRepository();
        return repository;
    }

    public static void destroyRepository() {
        repository = null;
    }

    public MutableLiveData<String> initializeData() {
        return entityLiveData;
    }

    public SingleLiveEvent<WikiSummary> initializeWikiData() {
        return wikiSummary;
    }

    public MutableLiveData<CoverArt> initializeCoverArtData() {
        return coverArtData;
    }

    public void fetchData(String entity, String MBID, String params) {
        service.lookupEntityData(entity, MBID, params).enqueue(lookupCallback);
    }

    public void getWikiSummary(String string, int method) {
        if (method == METHOD_WIKIPEDIA_URL)
            fetchWiki(string);
        else
            fetchWikiData(string);
    }

    private void fetchWiki(String title) {
        service.getWikipediaSummary(title).enqueue(new Callback<WikiSummary>() {
            @Override
            public void onResponse(@NonNull Call<WikiSummary> call, @NonNull Response<WikiSummary> response) {
                WikiSummary wiki = response.body();
                wikiSummary.setValue(wiki);
            }

            @Override
            public void onFailure(@NonNull Call<WikiSummary> call, @NonNull Throwable t) {

            }
        });
    }

    private void fetchWikiData(String id) {
        service.getWikipediaLink(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                String title = "";
                try {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JsonElement element = new JsonParser().parse(jsonResponse);
                    JsonObject result = element.getAsJsonObject()
                            .getAsJsonObject("entities").getAsJsonObject(id);
                    WikiDataResponse wikiDataResponse = new Gson().fromJson(result, WikiDataResponse.class);
                    title = Objects.requireNonNull(wikiDataResponse.getSitelinks().get("enwiki")).getTitle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fetchWiki(title);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * For a given release ID, fetches the cover arts and updates the release w¡th that info
     *
     * @param release Release for which the cover art is to be retrieved
     */
    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        return service.getCoverArt(release.getMbid());
    }

    public void getCoverArt(String MBID) {
        service.getCoverArtAll(MBID).enqueue(new Callback<CoverArt>() {
            @Override
            public void onResponse(@NonNull Call<CoverArt> call, @NonNull Response<CoverArt> response) {
                CoverArt coverArt = response.body();
                coverArtData.setValue(coverArt);
            }

            @Override
            public void onFailure(@NonNull Call<CoverArt> call, @NonNull Throwable t) {

            }
        });
    }
}
