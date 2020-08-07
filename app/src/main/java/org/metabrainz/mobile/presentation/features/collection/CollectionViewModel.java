package org.metabrainz.mobile.presentation.features.collection;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.CollectionRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.adapters.ResultItemUtils;
import org.metabrainz.mobile.util.Resource;

import java.util.List;

public class CollectionViewModel extends ViewModel {

    private CollectionRepository repository;

    @ViewModelInject
    public CollectionViewModel(CollectionRepository repository) {
        this.repository = repository;
    }

    private static Resource<List<ResultItem>> toResultItemsList(MBEntityType entity, Resource<String> response) {
        Resource<List<ResultItem>> resource;
        if (response != null && response.getStatus() == Resource.Status.SUCCESS) {
            try {
                List<ResultItem> resultItems = ResultItemUtils
                        .getJSONResponseAsResultItemList(response.getData(), entity);
                resource = new Resource<>(Resource.Status.SUCCESS, resultItems);
            } catch (Exception e) {
                e.printStackTrace();
                resource = new Resource<>(Resource.Status.FAILED, null);
            }
        } else
            resource = new Resource<>(Resource.Status.FAILED, null);
        return resource;
    }

    LiveData<Resource<List<Collection>>> fetchCollectionData(String editor, boolean fetchPrivate) {
        return repository.fetchCollections(editor, fetchPrivate);
    }

    LiveData<Resource<List<ResultItem>>> fetchCollectionDetails(MBEntityType entity, String id) {
        return Transformations.map(repository.fetchCollectionDetails(entity.name, id),
                response -> CollectionViewModel.toResultItemsList(entity, response));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository = null;
    }
}
