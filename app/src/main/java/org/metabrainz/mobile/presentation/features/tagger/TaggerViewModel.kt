package org.metabrainz.mobile.presentation.features.tagger

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.QueryUtils
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.Track
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.util.*
import javax.inject.Inject

@HiltViewModel
class TaggerViewModel @Inject constructor(val repository: LookupRepository, val context: Application) : AndroidViewModel(context) {

    private val _taglibFetchedMetadata = MutableLiveData<AudioFile?>()
    val taglibFetchedMetadata: LiveData<AudioFile?> get() = _taglibFetchedMetadata

    private val _uri = MutableLiveData<Uri>()
    val uri:LiveData<Uri> get() = _uri

    val serverFetchedMetadata: LiveData<List<TagField>>
    private val matchedResult: LiveData<ComparisonResult>

    val serverCoverArt: LiveData<Resource<CoverArt>>

    fun setTaglibFetchedMetadata(metadata: AudioFile?) {
        _taglibFetchedMetadata.value = metadata!!
    }

    fun setURI(uri:Uri){
        _uri.value = uri
    }

    private fun chooseRecordingFromList(recordings: List<Recording>): ComparisonResult? {
        if (taglibFetchedMetadata.value == null) {
            return null
        }
        val recording = Metadata.createRecordingFromHashMap(taglibFetchedMetadata.value!!)
        var maxScore = 0.0
        var comparisionResult = ComparisonResult(0.0, null, null)
        for (searchResult in recordings) {
            val result = TaggerUtils.compareTracks(recording, searchResult)
            if (result.score > maxScore) {
                maxScore = result.score
                comparisionResult = result
            }
        }

        if (comparisionResult.releaseMbid != null && comparisionResult.releaseMbid!!.isNotEmpty()
                && comparisionResult.score > TaggerUtils.THRESHOLD) {
            return comparisionResult
        }
        return null
    }

    private fun displayMatchedRelease(release: Release): List<TagField> {
        var track: Track? = null
        if (release.media != null && release.media!!.isNotEmpty()) {
            for (media in release.media!!) {
                for (search in media.tracks) {
                    if (search.recording!!.mbid.equals(matchedResult.value?.trackMbid, true)) {
                        track = search
                    }
                }
            }
        }
        return Metadata.createTagFields(taglibFetchedMetadata.value, track, release)
    }

    fun saveMetadataTags(tags: HashMap<String, String>): Boolean {
        Log.i("filepath",_uri.value.toString())
        var result = false
        context.contentResolver.openFileDescriptor(Uri.parse(_uri.value.toString()), "rw")?.use {
            result = KTagLib.writeMetadata(it.detachFd(), tags)

            Log.i("resulttag",result.toString())
        }
        return result
    }

    init {
        matchedResult = map(switchMap(taglibFetchedMetadata) {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                val result = repository.fetchRecordings(QueryUtils.getQuery(Metadata.getDefaultTagList(it)))
                if (result.status == Resource.Status.SUCCESS) {
                    emit(result.data)
                }
            }
        }) { recordings ->
            if(recordings!=null){
                chooseRecordingFromList(recordings)
            }
            else{
                null
            }
        }

        serverFetchedMetadata = map(switchMap(matchedResult) { comparisonResult ->
            if (comparisonResult != null) {
                liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                    val result = repository.fetchMatchedRelease(comparisonResult.releaseMbid)
                    if (result.status == Resource.Status.SUCCESS) {
                        emit(result.data)
                    }
                }
            }
            else{
                null
            }
        }) { release ->
            if (release != null) {
                displayMatchedRelease(release)
            }
            else {
                null
            }
        }

        serverCoverArt = map(switchMap(matchedResult) { comparisonResult ->
                if (comparisonResult != null) {
                    liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                        val result = repository.fetchCoverArt(comparisonResult.releaseMbid!!)
                        if (result.status == Resource.Status.SUCCESS) {
                            emit(result.data)
                        }
                    }
                }
                else{
                    null
                }
        }){ coverArt ->
            Resource(Resource.Status.SUCCESS,coverArt)
        }
    }
}