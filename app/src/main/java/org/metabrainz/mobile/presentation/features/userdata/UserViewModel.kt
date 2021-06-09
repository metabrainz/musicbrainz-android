package org.metabrainz.mobile.presentation.features.userdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity

class UserViewModel : ViewModel() {

    private val entityData = MutableLiveData<MBEntity>()

    val userData: LiveData<MBEntity> = entityData

    fun setUserData(entity: MBEntity) {
        entityData.value = entity
    }
}