package org.metabrainz.mobile.presentation.features

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.util.Resource

abstract class LookupViewModel protected constructor(val repository: LookupRepository, val entity: MBEntityType) : ViewModel() {

    protected val mbid: MutableLiveData<String> = MutableLiveData()

    protected val jsonLiveData: LiveData<Resource<String>>

    fun setMBID(MBID: String?) {
        if (MBID != null && MBID.isNotEmpty())
            this.mbid.value = MBID
    }

    abstract val data: LiveData<out Resource<out MBEntity?>>

    init {
        jsonLiveData = mbid.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                if (it == null)
                    emit(Resource.getFailure(String::class.java))
                else
                    emit(repository.fetchData(entity.name, it, Constants.getDefaultParams(entity)))
            }
        }
    }
}