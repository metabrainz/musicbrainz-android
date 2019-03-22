package org.metabrainz.mobile.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.repository.LookupRepository;
import org.metabrainz.mobile.util.SingleLiveEvent;

import java.util.List;

public class ArtistViewModel extends ViewModel {

    private LookupRepository repository = LookupRepository.getRepository();
    private MutableLiveData<Artist> artistData;
    private SingleLiveEvent<ArtistWikiSummary> artistWiki;
    private MutableLiveData<List<Release>> releaseListLiveData;
    private String MBID;

    public ArtistViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty())this.MBID = MBID;
    }

    public MutableLiveData<Artist> initializeArtistData(){
        // Obtain live data from the repository if not already present
        if (artistData == null)
            artistData = repository.initializeArtistData();
        return artistData;
    }

    public void getArtistData(){
        // Call the repository to query the database to update the artist data
        repository.getArtist(MBID);
    }

    public void fetchCoverArtForRelease(Release release, SingleLiveEvent<Release> releaseLiveData) {
        // Ask the repository to fetch the cover art and update ArtistData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        repository.fetchCoverArtForRelease(release, releaseLiveData);
    }

    public MutableLiveData<List<Release>> initializeReleasesLiveData(){
        if (releaseListLiveData == null)
            releaseListLiveData = repository.initializeLiveData();
        return releaseListLiveData;
    }

    public void loadArtistWiki(String title, int method){
        repository.getArtistWikiSummary(title ,method);
    }

    public SingleLiveEvent<ArtistWikiSummary> initializeWikiData(){
        if (artistWiki == null)
            artistWiki = repository.initializeWikiData();
        return artistWiki;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LookupRepository.destroyRepository();
    }
}
