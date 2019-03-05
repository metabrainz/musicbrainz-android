package org.metabrainz.mobile.viewmodel;

import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.repository.LookupRepository;
import org.metabrainz.mobile.util.SingleLiveEvent;

public class ArtistViewModel extends ViewModel {

    private LookupRepository repository = LookupRepository.getRepository();
    private SingleLiveEvent<Artist> artistData;
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

    public SingleLiveEvent<Artist> getArtistData(){
        if (artistData == null || mbidHasChanged)
            artistData = loadArtistData();
        return artistData;
    }

    private SingleLiveEvent<Artist> loadArtistData(){
        return repository.getArtist(MBID);
    }

    public SingleLiveEvent<ArtistWikiSummary> getArtistWiki(String title, int method){
        if (artistWiki == null || mbidHasChanged)
            artistWiki = loadArtistWiki(title, method);
         return artistWiki;
    }

    private SingleLiveEvent<ArtistWikiSummary> loadArtistWiki(String title, int method){
        return repository.getArtistWikiSummary(title ,method);
    }

}
