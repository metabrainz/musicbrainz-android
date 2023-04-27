package org.metabrainz.android.ui.screens.base

import androidx.lifecycle.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.util.Constants
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.SUCCESS

abstract class LookupViewModel<T : org.metabrainz.android.model.mbentity.MBEntity> protected constructor(val repository: LookupRepository, val entity: MBEntityType) : ViewModel() {

    val mbid: MutableLiveData<String> = MutableLiveData()

    protected val jsonLiveData: LiveData<Resource<String>> = mbid.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            if (it == null) {
                emit(Resource.failure())
            }
            else{
                emit(repository.fetchData(entity.entity, it, Constants.getDefaultParams(entity)))
            }
        }
    }

    abstract val data: LiveData<out Resource<T>>

    inline fun <reified T> parseData(data: Resource<String>): Resource<T> {
        return try {
            if (data.status == SUCCESS) {
                Resource(SUCCESS, Gson().fromJson(data.data, T::class.java))
            }
            else {
                Resource.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failure()
        }
    }

}