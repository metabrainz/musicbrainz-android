package org.metabrainz.mobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.CoverArt;
import org.metabrainz.mobile.api.data.search.Image;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.repository.LookupRepository;
import org.metabrainz.mobile.util.Log;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArtistViewModel extends ViewModel {

    private LookupRepository repository = LookupRepository.getRepository();
    private MutableLiveData<List<Release>> releasesLiveData;
    private MutableLiveData<Artist> artistData;
    private MutableLiveData<CoverArt> coverData;
    private Artist artist;
    private SingleLiveEvent<ArtistWikiSummary> artistWiki;
    private String MBID;
    public boolean mbidHasChanged = true;

    public ArtistViewModel() {
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setMBID(String MBID) {
        if(MBID != null && !MBID.isEmpty()) {
            this.MBID = MBID;
            mbidHasChanged = true;
        } else mbidHasChanged = false;
    }

    public MutableLiveData<Artist> getArtistData(){
        if (artistData == null || mbidHasChanged)
            artistData = loadArtistData();
        return artistData;
    }

    private MutableLiveData<Artist> loadArtistData(){
        return repository.getArtist(MBID);
    }

    public SingleLiveEvent<ArtistWikiSummary> getArtistWiki(String title, int method){
        if (artistWiki == null || mbidHasChanged)
            artistWiki = loadArtistWiki(title, method);
         return artistWiki;
    }

    public void fetchCoverArtForRelease(String releaseId, int position) {
        // Ask the repository to fetch the cover art
        //repository.fetchCoverArtForRelease(releaseId);
        //final Image coverImage;

        //LiveData<CoverArt> coverData = repository.fetchCoverArtForRelease(releaseId);

//        Transformations.switchMap(coverData, (Function<CoverArt, LiveData<Artist>>) coverArt -> {
//            Log.d("LLEGÓ");
//            return repository.updateArtistWithRelease();
//        });

        repository.fetchCoverArtForRelease(releaseId, position);

        /*Transformations.switchMap(repository.fetchCoverArtForRelease(releaseId), coverArt -> {
            if (coverArt == null) {
                return artistData;
            }

            Artist artist = artistData.getValue();
            if (artist != null) {
                ArrayList<Release> releases = artist.getReleases();

                if (releases != null) {
                    Release release = releases.get(position);
                    release.setCoverArt(coverArt);
                    releases.set(position, release);
                    artist.setRelease(release, position);
                    artistData.setValue(artist);
                }
            }
            return artistData;
        });*/

//        artistData = Transformations.switchMap(
//                coverData, (Function<CoverArt, LiveData<Artist>>) coverArt -> {
//                    return repository.updateArtistWithRelease();
//                });
//
//        Transformations.switchMap(fetchCoverArtForRelease(releaseId, position), repository::updateArtistWithRelease);




        // Subscribe to its answer and map it to the release
    }

    private SingleLiveEvent<ArtistWikiSummary> loadArtistWiki(String title, int method){
        return repository.getArtistWikiSummary(title ,method);
    }

    private MutableLiveData<List<Release>> initializeListData(List<Release> releases, int position){
        return repository.fetchCoverArt(releases,position);
    }

}
