package org.metabrainz.mobile.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.WikiDataResponse;
import org.metabrainz.mobile.api.data.search.CoverArt;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.api.webservice.Constants;
import org.metabrainz.mobile.api.webservice.LookupService;
import org.metabrainz.mobile.api.webservice.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookupRepository {
    private final static LookupService service = MusicBrainzServiceGenerator
            .createService(LookupService.class);
    private static LookupRepository repository;
    private static MutableLiveData<Artist> artistData;
    private static SingleLiveEvent<ArtistWikiSummary> artistWikiSummary;
    private static MutableLiveData<List<Release>> releaseListLiveData;

    public static final int METHOD_WIKIPEDIA_URL = 0;
    public static final int METHOD_WIKIDATA_ID = 1;

    private LookupRepository() {

        artistData = new MutableLiveData<>();
        artistWikiSummary = new SingleLiveEvent<>();
        releaseListLiveData = new SingleLiveEvent<>();
    }

    public static LookupRepository getRepository() {
        if (repository == null) repository = new LookupRepository();
        return repository;
    }

    public static void destroyRepository(){
        repository = null;
    }

    public MutableLiveData<Artist> initializeArtistData(){
        return artistData;
    }

    public SingleLiveEvent<ArtistWikiSummary> initializeWikiData(){ return artistWikiSummary;}

    public void getArtist(String MBID){
        if(App.isUserLoggedIn())
            fetchArtistWithUserData(MBID);
        else
            fetchArtist(MBID);
    }

    public void getArtistWikiSummary(String string, int method){
        if(method == METHOD_WIKIPEDIA_URL)
            fetchArtistWiki(string);
        else
            fetchArtistWikiData(string);
    }

    //TODO: Implement artist user data fetch
    private void fetchArtistWithUserData(String MBID){
    }

    private void fetchArtist(String MBID){
        service.lookupArtist(MBID, Constants.LOOKUP_ARTIST_PARAMS).enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(Call<Artist> call, Response<Artist> response) {
                    Artist artist = response.body();
                    artistData.setValue(artist);
            }

            @Override
            public void onFailure(Call<Artist> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Release>> initializeLiveData(){
        return releaseListLiveData;
    }

    private void fetchArtistWiki(String title){
        service.getWikipediaSummary(title).enqueue(new Callback<ArtistWikiSummary>() {
            @Override
            public void onResponse(Call<ArtistWikiSummary> call, Response<ArtistWikiSummary> response) {
                ArtistWikiSummary wiki = response.body();
                artistWikiSummary.setValue(wiki);
            }
            @Override
            public void onFailure(Call<ArtistWikiSummary> call, Throwable t) {

            }
        });
    }

    private void fetchArtistWikiData(String id){
        service.getWikipediaLink(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String title="";
                try {
                    String jsonResponse = response.body().string();
                    JsonElement element = new JsonParser().parse(jsonResponse);
                    JsonObject result = element.getAsJsonObject()
                            .getAsJsonObject("entities").getAsJsonObject(id);
                    WikiDataResponse wikiDataResponse = new Gson().fromJson(result, WikiDataResponse.class);
                    title = wikiDataResponse.getSitelinks().get("enwiki").getTitle();
                }catch (Exception e){e.printStackTrace();}
                fetchArtistWiki(title);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    /**
     * For a given release ID, fetches the cover arts and updates the release w¡th that info
     * @param release Release for which the cover art is to be retrieved
     */
    public Single<CoverArt> fetchCoverArtForRelease(Release release){
        return service.getCoverArt(release.getMbid());
    }
}
