package org.metabrainz.mobile.presentation.features.artist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.ArtistLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.ArtistWikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.SingleLiveEvent;

import io.reactivex.Single;

public class ArtistViewModel extends ViewModel {

    private ArtistLookupRepository repository = ArtistLookupRepository.getRepository();
    private MutableLiveData<Artist> artistData;
    private SingleLiveEvent<ArtistWikiSummary> artistWiki;
    private String MBID;

    public ArtistViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public MutableLiveData<Artist> initializeArtistData() {
        // Obtain live data from the repository if not already present
        if (artistData == null)
            artistData = repository.initializeArtistData();
        return artistData;
    }

    public void getArtistData(boolean isLoggedIn) {
        // Call the repository to query the database to update the artist data
        repository.getArtist(MBID, isLoggedIn);
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ArtistData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    public void loadArtistWiki(String title, int method) {
        repository.getArtistWikiSummary(title, method);
    }

    public SingleLiveEvent<ArtistWikiSummary> initializeWikiData() {
        if (artistWiki == null)
            artistWiki = repository.initializeWikiData();
        return artistWiki;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ArtistLookupRepository.destroyRepository();
    }
}
