package org.metabrainz.mobile.presentation.features;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;

public abstract class LookupViewModel extends ViewModel {

    protected LookupRepository repository = LookupRepository.getRepository();
    protected String MBID;
    protected MBEntities entity;
    protected LiveData<? extends MBEntity> liveData;

    protected LookupViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public LiveData<? extends MBEntity> initializeData() {
        return liveData;
    }

    public void fetchData() {
        repository.fetchData(entity.name, MBID, Constants.getDefaultParams(entity));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LookupRepository.destroyRepository();
    }
}
