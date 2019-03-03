package org.metabrainz.mobile.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.App;
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

    private LookupRepository() {
        artistData = new MutableLiveData<>();
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
}
