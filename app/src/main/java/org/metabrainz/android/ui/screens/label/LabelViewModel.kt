package org.metabrainz.android.ui.screens.label

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import org.metabrainz.android.model.mbentity.Label
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.ui.screens.base.LookupViewModel
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(repository: LookupRepository) : LookupViewModel<Label>(repository, MBEntityType.LABEL) {
    override val data: LiveData<Resource<Label>> = jsonLiveData.map { parseData(it) }
}