package org.metabrainz.android.ui.screens.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.model.mbentity.Recording
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.ui.screens.base.LookupViewModel
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(repository: LookupRepository) : LookupViewModel<Recording>(repository, MBEntityType.RECORDING) {
    override val data: LiveData<Resource<Recording>> = jsonLiveData.map { parseData(it) }
}