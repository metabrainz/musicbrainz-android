package org.metabrainz.mobile.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.webservice.Constants;
import org.metabrainz.mobile.api.webservice.LookupService;
import org.metabrainz.mobile.api.webservice.MusicBrainzServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LookupRepository {
    private final static LookupService service = MusicBrainzServiceGenerator
            .createService(LookupService.class);
    private static LookupRepository repository;
    private final MutableLiveData<Artist> artistData;
    private final MutableLiveData<ArtistWikiSummary> artistWikiSummary;

    private LookupRepository() {
        artistData = new MutableLiveData<>();
        artistWikiSummary = new MutableLiveData<>();
    }

    public static LookupRepository getRepository() {
        if (repository == null) repository = new LookupRepository();
        return repository;
    }

    public MutableLiveData<Artist> getArtist(String MBID){
        if(App.isUserLoggedIn())
            fetchArtistWithUserData(MBID);
        else
            fetchArtist(MBID);
        return artistData;
    }

    public MutableLiveData<ArtistWikiSummary> getArtistWikiSummary(String title){
        fetchArtistWiki(title);
        return artistWikiSummary;
    }

    private void fetchArtistWithUserData(String MBID){
    }

    private void fetchArtist(String MBID){
        service.lookupArtist(MBID, Constants.LOOKUP_ARTIST_PARAMS).enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(Call<Artist> call, Response<Artist> response) {
                    Artist artist = response.body();
                    artistData.postValue(artist);
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
                artistWikiSummary.postValue(wiki);
            }

            @Override
            public void onFailure(Call<ArtistWikiSummary> call, Throwable t) {

            }
        });
    }
}
