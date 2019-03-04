package org.metabrainz.mobile.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.repository.LookupRepository;

public class ArtistViewModel extends ViewModel {
    private LookupRepository repository = LookupRepository.getRepository();
    private MutableLiveData<Artist> artistData;
    private MutableLiveData<ArtistWikiSummary> artistWiki;
    private String MBID;

    public ArtistViewModel() {
    }

    public void setMBID(String MBID) {
        this.MBID = MBID;
    }

    public MutableLiveData<Artist> getArtistData(){
        if (artistData == null)
            artistData = loadArtistData();
        return artistData;
    }

    private MutableLiveData<Artist> loadArtistData(){
        return repository.getArtist(MBID);
    }

    public MutableLiveData<ArtistWikiSummary> getArtistWiki(String title){
        if (artistWiki == null)
            artistWiki = loadArtistWiki(title);
        return artistWiki;
    }

    private MutableLiveData<ArtistWikiSummary> loadArtistWiki(String title){
        return repository.getArtistWikiSummary(title);
    }

}
