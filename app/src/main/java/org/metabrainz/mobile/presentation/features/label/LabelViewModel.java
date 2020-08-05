package org.metabrainz.mobile.presentation.features.label;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.LookupViewModel;
import org.metabrainz.mobile.util.Resource;

import io.reactivex.Single;

public class LabelViewModel extends LookupViewModel {

    private final LiveData<Resource<Label>> liveData;

    @ViewModelInject
    public LabelViewModel(LookupRepository repository) {
        super(repository);
        entity = MBEntityType.LABEL;
        liveData = Transformations.map(jsonLiveData, LabelViewModel::toLabel);
    }

    private static Resource<Label> toLabel(Resource<String> data) {
        Resource<Label> resource;
        try {
            if (data != null && data.getStatus() == Resource.Status.SUCCESS) {
                Label label = new Gson().fromJson(data.getData(), Label.class);
                resource = new Resource<>(Resource.Status.SUCCESS, label);
            } else
                resource = Resource.getFailure(Label.class);
        } catch (Exception e) {
            e.printStackTrace();
            resource = Resource.getFailure(Label.class);
        }
        return resource;
    }

    @Override
    public LiveData<Resource<Label>> getData() {
        return liveData;
    }

    Single<CoverArt> fetchCoverArtForRelease(Release release) {
        // Ask the repository to fetch the cover art and update LabelData LiveData
        // Whoever is observing that LiveData, will receive the release with the cover art
        return repository.fetchCoverArtForRelease(release);
    }
}
