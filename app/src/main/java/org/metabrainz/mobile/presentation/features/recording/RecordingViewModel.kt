package org.metabrainz.mobile.presentation.features.recording

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.presentation.features.base.LookupViewModel
import org.metabrainz.mobile.util.Resource

class RecordingViewModel @ViewModelInject constructor(repository: LookupRepository)
    : LookupViewModel<Recording>(repository, MBEntityType.RECORDING) {
    override val data: LiveData<Resource<Recording>> = jsonLiveData.map { parseData(it) }
}