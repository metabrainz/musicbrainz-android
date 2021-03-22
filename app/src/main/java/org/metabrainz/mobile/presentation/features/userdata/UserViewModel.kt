package org.metabrainz.mobile.presentation.features.userdata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<MBEntity> entityData = new MutableLiveData<>();

    public UserViewModel() {
    }

    LiveData<MBEntity> getUserData() {
        return entityData;
    }

    public void setUserData(MBEntity entity) {
        entityData.setValue(entity);
    }
}
