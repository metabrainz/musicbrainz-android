package org.metabrainz.android.ui.screens.userdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.metabrainz.android.model.mbentity.MBEntity

class UserViewModel : ViewModel() {

    private val entityData = MutableLiveData<MBEntity>()

    val userData: LiveData<MBEntity> = entityData

    fun setUserData(entity: MBEntity) {
        entityData.value = entity
    }
}