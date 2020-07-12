package org.metabrainz.mobile.presentation.features.artist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.LookupViewModel;

public class ArtistViewModel extends LookupViewModel {

    private final LiveData<WikiSummary> wikiSummary;
    private final LiveData<Artist> liveData;

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
}
