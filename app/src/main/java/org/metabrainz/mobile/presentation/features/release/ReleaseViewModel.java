package org.metabrainz.mobile.presentation.features.release;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.Resource;

public class ReleaseViewModel extends LookupViewModel {

    private final LiveData<CoverArt> coverArtData;
    private final LiveData<Resource<Release>> liveData;

    @ViewModelInject
    public ReleaseViewModel(LookupRepository repository) {
        super(repository);
        entity = MBEntityType.RELEASE;
        liveData = Transformations.map(jsonLiveData, ReleaseViewModel::toRelease);
        coverArtData = Transformations.switchMap(MBID, repository::fetchCoverArt);
    }

    private static Resource<Release> toRelease(Resource<String> data) {
        Resource<Release> resource;
        try {
            if (data != null && data.getStatus() == Resource.Status.SUCCESS) {
                Release release = new Gson().fromJson(data.getData(), Release.class);
                resource = new Resource<>(Resource.Status.SUCCESS, release);
            } else
                resource = Resource.getFailure(Release.class);
        } catch (Exception e) {
            e.printStackTrace();
            resource = Resource.getFailure(Release.class);
        }
        return resource;
    }

    @Override
    public LiveData<Resource<Release>> getData() {
        return liveData;
    }

    LiveData<CoverArt> fetchCoverArt() {
        return coverArtData;
    }

}
