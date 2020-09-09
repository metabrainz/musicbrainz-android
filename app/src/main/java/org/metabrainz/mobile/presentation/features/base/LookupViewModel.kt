package org.metabrainz.mobile.presentation.features.base

import androidx.lifecycle.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

abstract class LookupViewModel<T : MBEntity> protected constructor(
        val repository: LookupRepository, val entity: MBEntityType) : ViewModel() {

    val mbid: MutableLiveData<String> = MutableLiveData()

    protected val jsonLiveData: LiveData<Resource<String>>

    abstract val data: LiveData<out Resource<T>>

    inline fun <reified T> parseData(data: Resource<String>): Resource<T> {
        return try {
            if (data.status == SUCCESS)
                Resource(SUCCESS, Gson().fromJson(data.data, T::class.java))
            else
                Resource.getFailure(T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(T::class.java)
        }
    }

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