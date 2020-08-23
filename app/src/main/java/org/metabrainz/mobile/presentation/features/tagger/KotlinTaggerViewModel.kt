package org.metabrainz.mobile.presentation.features.tagger

import android.R.attr.track
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.simplecityapps.ktaglib.AudioFile
import org.metabrainz.mobile.data.repository.TaggerRepository
import org.metabrainz.mobile.data.sources.QueryUtils
import org.metabrainz.mobile.data.sources.api.entities.Track
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.util.ComparisionResult
import org.metabrainz.mobile.util.Log
import org.metabrainz.mobile.util.Metadata
import org.metabrainz.mobile.util.TaggerUtils
import javax.inject.Inject


class KotlinTaggerViewModel
@ViewModelInject constructor(val repository: TaggerRepository) : ViewModel() {

    val taglibFetchedMetadata = MutableLiveData<AudioFile>()
    val serverFetchedMetadata: LiveData<AudioFile>
    private val matchedResult: LiveData<ComparisionResult>

    fun setTaglibFetchedMetadata(metadata: AudioFile?) {
        taglibFetchedMetadata.value = metadata
    }

    private fun chooseRecordingFromList(recordings: List<Recording?>): ComparisionResult? {
        val recording = Metadata.getRecordingFromFile(taglibFetchedMetadata.value)
        var maxScore = 0.0
        var comparisionResult = ComparisionResult(0.0, null, null)
        for (searchResult in recordings) {
            val result = TaggerUtils.compareTracks(recording, searchResult)
            if (result.score > maxScore) {
                maxScore = result.score
                comparisionResult = result
            }
        }

        Log.d(recordings.toString())

        if (comparisionResult.releaseMbid != null
                && comparisionResult.releaseMbid.isNotEmpty()
                && comparisionResult.score > TaggerUtils.THRESHOLD)
            return comparisionResult
        return null
    }

    private fun displayMatchedRelease(release: Release?) : AudioFile {
        var track : Track? = null
        if (release?.media != null && release.media.isNotEmpty())
            for (media in release.media)
                for (search in media.tracks)
                    if (search.recording.mbid.equals(matchedResult.value?.trackMbid, true))
                        track = search
        return Metadata.getAudioFileFromTrack(track)
    }

    init {
        matchedResult = map(switchMap(taglibFetchedMetadata)
        { repository.fetchRecordings(QueryUtils.getQuery(Metadata.getDefaultTagList(it))) })
        { chooseRecordingFromList(it) }

        serverFetchedMetadata = map(switchMap(matchedResult)
        { if (it != null) repository.fetchMatchedRelease(it.releaseMbid) else null })
        { if (it != null) displayMatchedRelease(it) else null }
    }
}