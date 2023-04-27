package org.metabrainz.android.ui.screens.release_list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.model.entities.CoverArt
import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject

@HiltViewModel
class ReleaseListViewModel @Inject constructor(val repository: LookupRepository) : ViewModel() {
    val releaseList = MutableLiveData<List<Release>>()

    fun setReleases(releases: List<Release>) {
        releaseList.value = releases
    }

    fun fetchCoverArtForRelease(release: Release): LiveData<org.metabrainz.android.model.entities.CoverArt> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = repository.fetchCoverArt(release.mbid!!)
            if (result.status == SUCCESS) {
                emit(result.data!!)
            }
        }
    }

}