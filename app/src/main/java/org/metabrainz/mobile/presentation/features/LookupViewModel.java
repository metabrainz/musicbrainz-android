package org.metabrainz.mobile.presentation.features;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;

public abstract class LookupViewModel extends ViewModel {

    protected LookupRepository repository = LookupRepository.getRepository();
    protected MutableLiveData<String> MBID;
    protected MBEntities entity;
    protected LiveData<String> jsonLiveData;

    protected LookupViewModel() {
        MBID = new MutableLiveData<>();
        jsonLiveData = Transformations.switchMap(MBID,
                id -> repository.fetchData(entity.name, id, Constants.getDefaultParams(entity)));
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty())
            this.MBID.setValue(MBID);
    }

    public abstract LiveData<? extends MBEntity> getData();

    @Override
    protected void onCleared() {
        super.onCleared();
        LookupRepository.destroyRepository();
    }
}
