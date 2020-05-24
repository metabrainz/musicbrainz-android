package org.metabrainz.mobile.presentation.features.artist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.SingleLiveEvent;

import io.reactivex.Single;

public class ArtistViewModel extends LookupViewModel {

    private SingleLiveEvent<WikiSummary> artistWiki;
    private LiveData<Artist> artistData = Transformations.map(repository.initializeData(),
            data -> new Gson().fromJson(data, Artist.class));

    public ArtistViewModel() {
    }

    public LiveData<Artist> initializeData() {
        return artistData;
    }

    @Override
    public void fetchData() {
        repository.fetchData("artist", MBID, Constants.LOOKUP_ARTIST_PARAMS);
    }

    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ArtistData LiveData
        // Whoever is observing that L
        // iveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    public void loadArtistWiki(String title, int method) {
        repository.getWikiSummary(title, method);
    }

    public SingleLiveEvent<WikiSummary> initializeWikiData() {
        if (artistWiki == null)
            artistWiki = repository.initializeWikiData();
        return artistWiki;
    }

}
