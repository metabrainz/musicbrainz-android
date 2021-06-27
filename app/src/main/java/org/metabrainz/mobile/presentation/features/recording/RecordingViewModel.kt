package org.metabrainz.mobile.presentation.features.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.presentation.features.base.LookupViewModel
import org.metabrainz.mobile.util.Resource
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(repository: LookupRepository)
    : LookupViewModel<Recording>(repository, MBEntityType.RECORDING) {
    override val data: LiveData<Resource<Recording>> = jsonLiveData.map { parseData(it) }
}