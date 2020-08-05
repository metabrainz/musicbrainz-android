package org.metabrainz.mobile.presentation.features.release_group;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.Link;
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.Resource;

public class ReleaseGroupViewModel extends LookupViewModel {

    private final LiveData<Resource<WikiSummary>> wikiSummary;
    private final LiveData<Resource<ReleaseGroup>> liveData;

    @ViewModelInject
    public ReleaseGroupViewModel(LookupRepository repository) {
        super(repository);
        entity = MBEntityType.RELEASE_GROUP;
        liveData = Transformations.map(jsonLiveData, ReleaseGroupViewModel::toReleaseGroup);
        wikiSummary = Transformations.switchMap(liveData, this::fetchWikiSummary);
    }

    private static Resource<ReleaseGroup> toReleaseGroup(Resource<String> data) {
        Resource<ReleaseGroup> resource;
        try {
            if (data != null && data.getStatus() == Resource.Status.SUCCESS) {
                ReleaseGroup releaseGroup = new Gson().fromJson(data.getData(), ReleaseGroup.class);
                resource = new Resource<>(Resource.Status.SUCCESS, releaseGroup);
            } else
                resource = Resource.getFailure(ReleaseGroup.class);
        } catch (Exception e) {
            e.printStackTrace();
            resource = Resource.getFailure(ReleaseGroup.class);
        }
        return resource;
    }

    @Override
    public LiveData<Resource<ReleaseGroup>> getData() {
        return liveData;
    }

    LiveData<Resource<WikiSummary>> getWikiData() {
        return wikiSummary;
    }

    private LiveData<Resource<WikiSummary>> fetchWikiSummary(Resource<ReleaseGroup> resource) {
        String title = "";
        int method = -1;
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            ReleaseGroup releaseGroup = resource.getData();
            for (Link link : releaseGroup.getRelations()) {
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

        if (title.isEmpty()) {
            MutableLiveData<Resource<WikiSummary>> liveData = new MutableLiveData<>();
            liveData.setValue(Resource.getFailure(WikiSummary.class));
            return liveData;
        }

        return repository.fetchWikiSummary(title, method);
    }
}
