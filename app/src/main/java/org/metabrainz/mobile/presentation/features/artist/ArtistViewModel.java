package org.metabrainz.mobile.presentation.features.artist;

import androidx.lifecycle.Transformations;

import com.google.gson.Gson;
<<<<<<< HEAD
=======

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.SingleLiveEvent;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class ArtistViewModel extends LookupViewModel {

<<<<<<< HEAD
    private LiveData<WikiSummary> wikiSummary;
    private LiveData<Artist> liveData;

    public ArtistViewModel() {
        entity = MBEntityType.ARTIST;
        liveData = Transformations.map(jsonLiveData, data -> new Gson().fromJson(data, Artist.class));
        wikiSummary = Transformations.switchMap(liveData, this::fetchWikiSummary);
    }

    @Override
    public LiveData<Artist> getData() {
        return liveData;
    }

    LiveData<WikiSummary> getWikiData() {
        return wikiSummary;
    }

    private LiveData<WikiSummary> fetchWikiSummary(Artist artist) {
        String title = "";
        int method = -1;
        if (artist != null) {
            for (Link link : artist.getRelations()) {
                if (link.getType().equals("wikipedia")) {
                    title = link.getPageTitle();
                    method = LookupRepository.METHOD_WIKIPEDIA_URL;
                    break;
                }
                if (link.getType().equals("wikidata")) {
                    title = link.getPageTitle();
                    method = LookupRepository.METHOD_WIKIDATA_ID;
                    break;
                }
            }
        }

        return repository.fetchWikiSummary(title, method);
    }
=======
    private SingleLiveEvent<WikiSummary> artistWiki;

    public ArtistViewModel() {
        entity = MBEntities.ARTIST;
        liveData = Transformations.map(repository.initializeData(),
                data -> new Gson().fromJson(data, Artist.class));
    }

    Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update ArtistData LiveData
        // Whoever is observing that L
        // iveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }

    void loadArtistWiki(String title, int method) {
        repository.getWikiSummary(title, method);
    }

    SingleLiveEvent<WikiSummary> initializeWikiData() {
        if (artistWiki == null)
            artistWiki = repository.initializeWikiData();
        return artistWiki;
    }

>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
}
