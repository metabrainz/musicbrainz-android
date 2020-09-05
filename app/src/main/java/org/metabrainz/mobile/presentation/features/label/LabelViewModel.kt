package org.metabrainz.mobile.presentation.features.label

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.presentation.features.LookupViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

class LabelViewModel @ViewModelInject constructor(repository: LookupRepository)
    : LookupViewModel(repository, MBEntityType.LABEL) {
    override val data: LiveData<Resource<Label>>

    private fun toLabel(data: Resource<String>): Resource<Label> {
        return try {
            if (data.status == SUCCESS)
                Resource(SUCCESS, Gson().fromJson(data.data, Label::class.java))
            else
                Resource.getFailure(Label::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(Label::class.java)
        }
    }

    init {
        data = Transformations.map(jsonLiveData) { toLabel(it) }
    }
}