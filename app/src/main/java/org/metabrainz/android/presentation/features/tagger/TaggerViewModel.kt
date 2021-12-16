package org.metabrainz.android.presentation.features.tagger

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.sources.QueryUtils
import org.metabrainz.android.data.sources.api.entities.CoverArt
import org.metabrainz.android.data.sources.api.entities.Track
import org.metabrainz.android.data.sources.api.entities.mbentity.Recording
import org.metabrainz.android.data.sources.api.entities.mbentity.Release
import org.metabrainz.android.util.ComparisonResult
import org.metabrainz.android.util.Metadata
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.TaggerUtils
import javax.inject.Inject

@HiltViewModel
class TaggerViewModel @Inject constructor(val repository: LookupRepository, val context: Application) : AndroidViewModel(context) {

    private val _taglibFetchedMetadata = MutableLiveData<AudioFile?>()
    val taglibFetchedMetadata: LiveData<AudioFile?> get() = _taglibFetchedMetadata

    private val _uri = MutableLiveData<Uri>()
    val uri:LiveData<Uri> get() = _uri

    val serverFetchedMetadata: LiveData<Resource<List<TagField>>>
    private val matchedResult: LiveData<Resource<ComparisonResult>>

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

        if (comparisionResult.releaseMbid != null && comparisionResult.releaseMbid!!.isNotEmpty() && comparisionResult.score > TaggerUtils.THRESHOLD) {
            return comparisionResult
        }
        return null
    }

    private fun displayMatchedRelease(release: Release): Resource<List<TagField>> {
        var track: Track? = null
        if (release.media != null && release.media!!.isNotEmpty()) {
            for (media in release.media!!) {
                for (search in media.tracks) {
                    if (search.recording!!.mbid.equals(matchedResult.value?.data!!.trackMbid, true)) {
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
                emit(Resource.loading())
                val result = repository.fetchRecordings(QueryUtils.getQuery(Metadata.getDefaultTagList(it)))
                emit(result)
            }
        }) { resource ->
            //Handling the status of the api call
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    if(resource.data !=null){
                        Resource(Resource.Status.SUCCESS,chooseRecordingFromList(resource.data))
                    }
                    else{
                        null
                    }
                }
                Resource.Status.LOADING -> {
                    Resource.loading()
                }
                Resource.Status.FAILED -> {
                    Resource.failure()
                }
            }
        }

        serverFetchedMetadata = map(switchMap(matchedResult){
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(Resource.loading())
                val result = repository.fetchMatchedRelease(it.data?.releaseMbid)
                emit(result)
            }
        })  { resource ->
            //Handling the status of the api call
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    displayMatchedRelease(resource.data!!)
                }
                Resource.Status.LOADING -> {
                    Resource.loading()
                }
                Resource.Status.FAILED -> {
                    Resource.failure()
                }
            }
        }

        serverCoverArt = map(switchMap(matchedResult) {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(Resource.loading())
                val result = repository.fetchCoverArt(it.data?.releaseMbid!!)
                emit(result)
            }
        })  { resource ->
            //Handling the status of the api call
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                   null
                }
                Resource.Status.LOADING -> {
                    Resource.loading()
                }
                Resource.Status.FAILED -> {
                    Resource.failure()
                }
            }
        }
    }
}