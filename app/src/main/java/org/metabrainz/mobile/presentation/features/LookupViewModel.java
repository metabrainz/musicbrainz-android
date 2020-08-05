package org.metabrainz.mobile.presentation.features;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.util.Resource;

public abstract class LookupViewModel extends ViewModel {

    protected LookupRepository repository;
    protected final MutableLiveData<String> MBID;
    protected MBEntityType entity;
    protected final LiveData<Resource<String>> jsonLiveData;

    protected LookupViewModel(LookupRepository repository) {
        this.repository = repository;
        MBID = new MutableLiveData<>();
        jsonLiveData = Transformations.switchMap(MBID,
                id -> repository.fetchData(entity.name, id, Constants.getDefaultParams(entity)));
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty())
            this.MBID.setValue(MBID);
    }

    public abstract LiveData<? extends Resource<? extends MBEntity>> getData();

    @Override
    protected void onCleared() {
        super.onCleared();
        this.repository = null;
    }
}
