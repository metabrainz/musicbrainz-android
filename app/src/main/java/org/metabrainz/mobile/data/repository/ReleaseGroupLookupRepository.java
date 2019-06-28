package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.metabrainz.mobile.data.Constants;
import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.ArtistWikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiDataResponse;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.Objects;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseGroupLookupRepository {
    public static final int METHOD_WIKIPEDIA_URL = 0;
    public static final int METHOD_WIKIDATA_ID = 1;
    private final static LookupService service = MusicBrainzServiceGenerator
            .createService(LookupService.class, true);
    private static ReleaseGroupLookupRepository repository;
    private static MutableLiveData<ReleaseGroup> releaseGroupData;
    private static SingleLiveEvent<ArtistWikiSummary> wikiSummary;
    private Callback<ReleaseGroup> releaseGroupCallback;

    private ReleaseGroupLookupRepository() {
        releaseGroupData = new MutableLiveData<>();
        wikiSummary = new SingleLiveEvent<>();
    }

    public static ReleaseGroupLookupRepository getRepository() {
        if (repository == null) repository = new ReleaseGroupLookupRepository();
        return repository;
    }

    public static void destroyRepository() {
        repository = null;
    }

    public MutableLiveData<ReleaseGroup> initializeReleaseGroupData() {
        return releaseGroupData;
    }

    public SingleLiveEvent<ArtistWikiSummary> initializeWikiData() {
        return wikiSummary;
    }

    public void getReleaseGroup(String MBID, boolean isLoggedIn) {
        releaseGroupCallback = new Callback<ReleaseGroup>() {
            @Override
            public void onResponse(Call<ReleaseGroup> call, Response<ReleaseGroup> response) {
                ReleaseGroup releaseGroup = response.body();
                releaseGroupData.setValue(releaseGroup);
            }

            @Override
            public void onFailure(Call<ReleaseGroup> call, Throwable t) {

            }
        };

        if (isLoggedIn) fetchReleaseGroupWithUserData(MBID);
        else fetchReleaseGroup(MBID);
    }

    public void getWikiSummary(String string, int method) {
        if (method == METHOD_WIKIPEDIA_URL)
            fetchReleaseGroupWiki(string);
        else
            fetchReleaseGroupWikiData(string);
    }

    private void fetchReleaseGroupWithUserData(String MBID) {
        service.lookupReleaseGroup(MBID, Constants.LOOKUP_RELEASE_GROUP_PARAMS + Constants.USER_DATA_PARAMS)
                .enqueue(releaseGroupCallback);
    }

    private void fetchReleaseGroup(String MBID) {
        service.lookupReleaseGroup(MBID, Constants.LOOKUP_RELEASE_GROUP_PARAMS).enqueue(releaseGroupCallback);
    }

    private void fetchReleaseGroupWiki(String title) {
        service.getWikipediaSummary(title).enqueue(new Callback<ArtistWikiSummary>() {
            @Override
            public void onResponse(@NonNull Call<ArtistWikiSummary> call, @NonNull Response<ArtistWikiSummary> response) {
                ArtistWikiSummary wiki = response.body();
                wikiSummary.setValue(wiki);
            }

            @Override
            public void onFailure(@NonNull Call<ArtistWikiSummary> call, @NonNull Throwable t) {

            }
        });
    }

    private void fetchReleaseGroupWikiData(String id) {
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
                fetchReleaseGroupWiki(title);
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
}
