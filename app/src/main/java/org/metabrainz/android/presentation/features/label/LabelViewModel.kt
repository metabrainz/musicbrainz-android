package org.metabrainz.android.presentation.features.label

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.sources.api.entities.mbentity.Label
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.presentation.features.base.LookupViewModel
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(repository: LookupRepository)
    : LookupViewModel<Label>(repository, MBEntityType.LABEL) {
    override val data: LiveData<Resource<Label>> = jsonLiveData.map { parseData(it) }
}