package org.metabrainz.mobile.presentation.features.label

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.presentation.features.base.LookupViewModel
import org.metabrainz.mobile.util.Resource
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(repository: LookupRepository)
    : LookupViewModel<Label>(repository, MBEntityType.LABEL) {
    override val data: LiveData<Resource<Label>> = jsonLiveData.map { parseData(it) }
}