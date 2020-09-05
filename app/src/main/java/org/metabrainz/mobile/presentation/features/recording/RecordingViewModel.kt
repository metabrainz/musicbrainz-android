package org.metabrainz.mobile.presentation.features.recording

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.presentation.features.LookupViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

class RecordingViewModel @ViewModelInject constructor(repository: LookupRepository)
    : LookupViewModel(repository, MBEntityType.RECORDING) {
    override val data: LiveData<Resource<Recording>>

    private fun toRecording(data: Resource<String>): Resource<Recording> {
        return try {
            if (data.status == SUCCESS)
                Resource(SUCCESS, Gson().fromJson(data.data, Recording::class.java))
            else
                Resource.getFailure(Recording::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(Recording::class.java)
        }
    }

    init {
        data = Transformations.map(jsonLiveData) { toRecording(it) }
    }
}