package org.metabrainz.mobile.presentation.features.release

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.presentation.features.LookupViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS


class ReleaseViewModel @ViewModelInject constructor(repository: LookupRepository) : LookupViewModel(repository, MBEntityType.RELEASE) {
    private val coverArtData: LiveData<CoverArt>

    override val data: LiveData<Resource<Release>>

    fun fetchCoverArt(): LiveData<CoverArt> {
        return coverArtData
    }

    private fun toRelease(data: Resource<String>): Resource<Release> {
        return try {
            if (data.status == SUCCESS)
                Resource(SUCCESS, Gson().fromJson(data.data, Release::class.java))
            else
                Resource.getFailure(Release::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(Release::class.java)
        }
    }

    init {
        data = jsonLiveData.map { toRelease(it) }
        coverArtData = mbid.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                val result = repository.fetchCoverArt(it)
                if (result.status == SUCCESS)
                    emit(result.data)
            }
        }
    }
}