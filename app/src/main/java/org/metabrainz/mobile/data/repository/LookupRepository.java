package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiDataResponse;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.Resource;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class LookupRepository {

    public static final int METHOD_WIKIPEDIA_URL = 0;
    public static final int METHOD_WIKIDATA_ID = 1;

    private final LookupService service;

    @Inject
    public LookupRepository(LookupService service) {
        this.service = service;
    }

    public LiveData<Resource<String>> fetchData(String entity, String MBID, String params) {
        MutableLiveData<Resource<String>> entityLiveData = new MutableLiveData<>();
        service.lookupEntityData(entity, MBID, params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        Resource<String> resource;
                        try {
                            String data = response.body().string();
                            resource = new Resource<>(Resource.Status.SUCCESS, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            resource = Resource.getFailure(String.class);
                        }
                        entityLiveData.setValue(resource);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        entityLiveData.setValue(Resource.getFailure(String.class));
                    }
                });
        return entityLiveData;
    }

    public SingleLiveEvent<Resource<WikiSummary>> fetchWikiSummary(String string, int method) {
        SingleLiveEvent<Resource<WikiSummary>> wikiSummary = new SingleLiveEvent<>();
        if (method == METHOD_WIKIPEDIA_URL)
            fetchWiki(string, wikiSummary);
        else
            fetchWikiData(string, wikiSummary);
        return wikiSummary;
    }

    private void fetchWiki(String title, SingleLiveEvent<Resource<WikiSummary>> wikiSummary) {
        service.getWikipediaSummary(title).enqueue(new Callback<WikiSummary>() {
            @Override
            public void onResponse(@NonNull Call<WikiSummary> call, @NonNull Response<WikiSummary> response) {
                Resource<WikiSummary> resource;
                try {
                    WikiSummary data = response.body();
                    resource = new Resource<>(Resource.Status.SUCCESS, data);
                } catch (Exception e) {
                    resource = Resource.getFailure(WikiSummary.class);
                }
                wikiSummary.setValue(resource);
            }

            @Override
            public void onFailure(@NonNull Call<WikiSummary> call, @NonNull Throwable t) {
                wikiSummary.setValue(Resource.getFailure(WikiSummary.class));
            }
        });
    }

    private void fetchWikiData(String id, SingleLiveEvent<Resource<WikiSummary>> wikiSummary) {
        service.getWikipediaLink(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JsonElement element = JsonParser.parseString(jsonResponse);
                    JsonObject result = element.getAsJsonObject()
                            .getAsJsonObject("entities").getAsJsonObject(id);
                    WikiDataResponse wikiDataResponse = new Gson().fromJson(result, WikiDataResponse.class);
                    String title = Objects.requireNonNull(wikiDataResponse.getSitelinks().get("enwiki")).getTitle();
                    fetchWiki(title, wikiSummary);
                } catch (Exception e) {
                    e.printStackTrace();
                    wikiSummary.setValue(Resource.getFailure(WikiSummary.class));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                wikiSummary.setValue(Resource.getFailure(WikiSummary.class));
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
