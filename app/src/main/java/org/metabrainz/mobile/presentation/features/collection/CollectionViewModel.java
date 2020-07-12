package org.metabrainz.mobile.presentation.features.collection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.CollectionRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.adapters.ResultItemUtils;

import java.util.List;

public class CollectionViewModel extends ViewModel {

    private static final CollectionRepository repository = CollectionRepository.getRepository();

    public CollectionViewModel() {
    }

    LiveData<List<Collection>> fetchCollectionData(String editor, boolean fetchPrivate) {
        return repository.fetchCollections(editor, fetchPrivate);
    }

    LiveData<List<ResultItem>> fetchCollectionDetails(MBEntityType entity, String id) {
        return Transformations.map(repository.fetchCollectionDetails(entity.name, id),
                response -> ResultItemUtils.getJSONResponseAsResultItemList(response, entity));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionRepository.destroyRepository();
    }
}
