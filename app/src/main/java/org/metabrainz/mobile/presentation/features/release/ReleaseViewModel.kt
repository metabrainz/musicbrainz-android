package org.metabrainz.mobile.presentation.features.release

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.presentation.features.base.LookupViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS


class ReleaseViewModel @ViewModelInject constructor(repository: LookupRepository) :
        LookupViewModel<Release>(repository, MBEntityType.RELEASE) {

    val coverArtData: LiveData<CoverArt>

    override val data: LiveData<Resource<Release>> = jsonLiveData.map { parseData(it) }

    init {
        coverArtData = mbid.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                val result = repository.fetchCoverArt(it)
                if (result.status == SUCCESS)
                    emit(result.data)
            }
        }
    }
}