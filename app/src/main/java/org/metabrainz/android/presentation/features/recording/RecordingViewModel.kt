package org.metabrainz.android.presentation.features.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.data.sources.api.entities.mbentity.Recording
import org.metabrainz.android.presentation.features.base.LookupViewModel
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(repository: LookupRepository) : LookupViewModel<Recording>(repository, MBEntityType.RECORDING) {
    override val data: LiveData<Resource<Recording>> = jsonLiveData.map { parseData(it) }
}