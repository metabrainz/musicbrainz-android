package org.metabrainz.mobile.presentation.features.artist;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.Resource;

public class ArtistViewModel extends LookupViewModel {

    private final LiveData<Resource<WikiSummary>> wikiSummary;
    private final LiveData<Resource<Artist>> liveData;

    @ViewModelInject
    public ArtistViewModel(LookupRepository repository) {
        super(repository);
        entity = MBEntityType.ARTIST;
        liveData = Transformations.map(jsonLiveData, ArtistViewModel::toArtist);
        wikiSummary = Transformations.switchMap(liveData, this::fetchWikiSummary);
    }

    private static Resource<Artist> toArtist(Resource<String> data) {
        Resource<Artist> resource;
        try {
            if (data != null && data.getStatus() == Resource.Status.SUCCESS) {
                Artist artist = new Gson().fromJson(data.getData(), Artist.class);
                resource = new Resource<>(Resource.Status.SUCCESS, artist);
            } else
                resource = Resource.getFailure(Artist.class);
        } catch (Exception e) {
            e.printStackTrace();
            resource = Resource.getFailure(Artist.class);
        }
        return resource;
    }

    @Override
    public LiveData<Resource<Artist>> getData() {
        return liveData;
    }

    public LiveData<Resource<WikiSummary>> getWikiData() {
        return wikiSummary;
    }

    private LiveData<Resource<WikiSummary>> fetchWikiSummary(Resource<Artist> resource) {
        String title = "";
        int method = -1;
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            Artist artist = resource.getData();
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
        }

        if (title.isEmpty()) {
            MutableLiveData<Resource<WikiSummary>> liveData = new MutableLiveData<>();
            liveData.setValue(Resource.getFailure(WikiSummary.class));
            return liveData;
        }

        return repository.fetchWikiSummary(title, method);
    }
}
