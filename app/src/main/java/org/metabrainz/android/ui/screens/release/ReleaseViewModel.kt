package org.metabrainz.android.ui.screens.release

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.model.entities.CoverArt
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.ui.screens.base.LookupViewModel
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject

@HiltViewModel
class ReleaseViewModel @Inject constructor(repository: LookupRepository) :
        LookupViewModel<Release>(repository, MBEntityType.RELEASE) {

    val coverArtData: LiveData<org.metabrainz.android.model.entities.CoverArt?> = mbid.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = repository.fetchCoverArt(it)
            if (result.status == SUCCESS) {
                emit(result.data)
            }
        }
    }

    override val data: LiveData<Resource<Release>> = jsonLiveData.map { parseData(it) }

}