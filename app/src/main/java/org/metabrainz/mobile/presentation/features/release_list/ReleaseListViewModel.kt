package org.metabrainz.mobile.presentation.features.release_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

class ReleaseListViewModel @ViewModelInject constructor(val repository: LookupRepository) : ViewModel() {
    val releaseList = MutableLiveData<List<Release>>()

    fun setReleases(releases: List<Release>) {
        releaseList.value = releases
    }

    fun fetchCoverArtForRelease(release: Release): LiveData<CoverArt> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = repository.fetchCoverArt(release.mbid)
            if (result.status == SUCCESS)
                emit(result.data)
        }
    }

}