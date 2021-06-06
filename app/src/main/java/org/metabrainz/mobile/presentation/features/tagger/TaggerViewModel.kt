package org.metabrainz.mobile.presentation.features.tagger

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.simplecityapps.ktaglib.KTagLib
import org.metabrainz.mobile.data.repository.TaggerRepository
import org.metabrainz.mobile.data.sources.QueryUtils
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.Track
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.util.ComparisionResult
import org.metabrainz.mobile.util.Metadata
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.TaggerUtils

class TaggerViewModel @ViewModelInject constructor(val repository: TaggerRepository, val context: Application) : AndroidViewModel(context) {

    private val _taglibFetchedMetadata = MutableLiveData<AudioFile?>()
    val taglibFetchedMetadata: LiveData<AudioFile?> get() = _taglibFetchedMetadata

    private val _uri = MutableLiveData<Uri>()
    val uri:LiveData<Uri> get() = _uri

    val serverFetchedMetadata: LiveData<List<TagField>>
    private val matchedResult: LiveData<ComparisionResult>

    var serverCoverArt: LiveData<Resource<CoverArt>> = MutableLiveData()

    fun setTaglibFetchedMetadata(metadata: AudioFile?) {
        _taglibFetchedMetadata.value = metadata!!
    }

    fun setURI(uri:Uri){
        _uri.value = uri
    }

    private fun chooseRecordingFromList(recordings: List<Recording>): ComparisionResult? {
        if (taglibFetchedMetadata.value == null) {
            return null
        }
        val recording = Metadata.createRecordingFromHashMap(taglibFetchedMetadata.value!!)
        var maxScore = 0.0
        var comparisionResult = ComparisionResult(0.0, null, null)
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
        matchedResult = map(switchMap(taglibFetchedMetadata)
        { repository.fetchRecordings(QueryUtils.getQuery(Metadata.getDefaultTagList(it))) })
        { chooseRecordingFromList(it) }

        serverFetchedMetadata = map(switchMap(matchedResult) { comparisonResult ->
            if (comparisonResult != null) {
                repository.fetchMatchedRelease(comparisonResult.releaseMbid)
            }
            else{
                null
            }
        }) { release ->
            if (release != null) {
                serverCoverArt = repository.fetchCoverArt(release.mbid)
                displayMatchedRelease(release)
            }
            else {
                null
            }
        }
    }
}