package org.metabrainz.mobile.repository;

import androidx.lifecycle.LiveData;
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
import org.metabrainz.mobile.util.Log;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Transformations;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookupRepository {
    private final static LookupService service = MusicBrainzServiceGenerator
            .createService(LookupService.class);
    private static LookupRepository repository;
    private final SingleLiveEvent<Artist> artistData;
    private final SingleLiveEvent<ArtistWikiSummary> artistWikiSummary;
    private final MutableLiveData<List<Release>> coverArtMutableLiveData;
    private final MutableLiveData<CoverArt> coverArtData;

    public static final int METHOD_WIKIPEDIA_URL = 0;
    public static final int METHOD_WIKIDATA_ID = 1;

    private LookupRepository() {
        artistData = new SingleLiveEvent<>();
        artistWikiSummary = new SingleLiveEvent<>();
        coverArtMutableLiveData  = new MutableLiveData<>();
        coverArtData  = new MutableLiveData<>();

        /*Transformations.switchMap(coverArtData, coverArt -> {
            if (coverArt == null) {
                return artistData;
            }

            Artist artist = artistData.getValue();
            if (artist != null) {
                ArrayList<Release> releases = artist.getReleases();

                if (releases != null) {
                    //Release release = releases.get(position);
                    //release.setCoverArt(coverArt);
                    //releases.set(position, release);
                    //artist.setRelease(release, position);
                    artistData.postValue(artist);
                }
            }
            return artistData;
        });*/
    }

    public static LookupRepository getRepository() {
        if (repository == null) repository = new LookupRepository();
        return repository;
    }

    public SingleLiveEvent<Artist> getArtist(String MBID){
        if(App.isUserLoggedIn())
            fetchArtistWithUserData(MBID);
        else
            fetchArtist(MBID);
        return artistData;
    }

    public LiveData<CoverArt> getCoverArtData() {
        return coverArtData;
    }

    public SingleLiveEvent<ArtistWikiSummary> getArtistWikiSummary(String string, int method){
        if(method == METHOD_WIKIPEDIA_URL)
            fetchArtistWiki(string);
        else
            fetchArtistWikiData(string);
        return artistWikiSummary;
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

    public MutableLiveData<List<Release>> fetchCoverArt(List<Release> releases, int position){
        Release release = releases.get(position);
        service.getCoverArt(release.getMbid()).enqueue(new Callback<CoverArt>() {
            @Override
            public void onResponse(Call<CoverArt> call, Response<CoverArt> response) {
                CoverArt art = response.body();
                release.setCoverArt(art);
                coverArtMutableLiveData.setValue(releases);
            }

            @Override
            public void onFailure(Call<CoverArt> call, Throwable t) {

            }
        });
        return coverArtMutableLiveData;
    }

    public MutableLiveData<CoverArt> fetchCoverArtForRelease(String releaseId, int position){
        if (artistData != null) {
            Log.d("HAY DATOS");
        }

        service.getCoverArt(releaseId).enqueue(new Callback<CoverArt>() {
            @Override
            public void onResponse(Call<CoverArt> call, Response<CoverArt> response) {
                CoverArt art = response.body();
                if (response.code() == 200) {
                    CoverArt coverArt = response.body();

                    Artist artist = artistData.getValue();
                    if (artist != null) {
                        ArrayList<Release> releases = artist.getReleases();

                        if (releases != null) {
                            Release release = releases.get(position);
                            release.setCoverArt(coverArt);
                            //releases.set(position, release);
                            artist.setRelease(release, position);
                            artistData.postValue(artist);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CoverArt> call, Throwable t) {
                Log.d(t.getLocalizedMessage());
            }
        });

        return coverArtData;
    }

    public LiveData<Artist> updateArtistWithRelease() {
        Log.d("ACTUALIZANDO");
        return artistData;
    }
}
