package org.metabrainz.mobile.presentation.features;

import androidx.lifecycle.LiveData;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
=======
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.LookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

public abstract class LookupViewModel extends ViewModel {

    protected LookupRepository repository = LookupRepository.getRepository();
<<<<<<< HEAD
<<<<<<< HEAD
    protected MutableLiveData<String> MBID;
    protected MBEntityType entity;
=======
    protected MutableLiveData<String> MBID;
    protected MBEntities entity;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
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
<<<<<<< HEAD
=======
    protected String MBID;
    protected String entity;

    protected LookupViewModel() {
    }

    public void setMBID(String MBID) {
        if (MBID != null && !MBID.isEmpty()) this.MBID = MBID;
    }
=======
>>>>>>> b793e03... Improve usage of live data and reactive patterns.

    public abstract LiveData<? extends MBEntity> initializeData();

    public abstract void fetchData();
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

    @Override
    protected void onCleared() {
        super.onCleared();
        LookupRepository.destroyRepository();
    }
}
