package org.metabrainz.mobile.presentation.features;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;

public abstract class LookupViewModel extends ViewModel {

    protected LookupRepository repository = LookupRepository.getRepository();
    protected String MBID;
    protected String entity;

    protected LookupViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }

    public abstract LiveData<? extends MBEntity> initializeData();

    public abstract void fetchData();

    @Override
    protected void onCleared() {
        super.onCleared();
        LookupRepository.destroyRepository();
    }
}
