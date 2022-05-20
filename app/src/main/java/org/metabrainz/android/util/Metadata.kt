package org.metabrainz.android.util

import org.metabrainz.android.data.sources.api.entities.*
import org.metabrainz.android.data.sources.api.entities.mbentity.Recording
import org.metabrainz.android.data.sources.api.entities.mbentity.Release
import org.metabrainz.android.presentation.features.tagger.AudioFile
import org.metabrainz.android.presentation.features.tagger.TagField

object Metadata {

    fun getDefaultTagList(metadata: AudioFile?): HashMap<String, String> {
        val defaultTagMap = HashMap<String, String>()
        defaultTagMap["title"] = metadata!!.allProperties["TITLE"] ?: ""
        defaultTagMap["artist"] = metadata.allProperties["ARTIST"] ?: ""
        defaultTagMap["release"] = metadata.allProperties["ALBUM"] ?: ""
        defaultTagMap["tnum"] = metadata.allProperties["TRACK"] ?: ""
        defaultTagMap["tracks"] = metadata.allProperties["TRACKTOTAL"] ?: ""
        Log.d(defaultTagMap.toString())
        return defaultTagMap
    }

    fun createRecordingFromHashMap(metadata: AudioFile?): Recording {
        val recording = Recording()
        recording.releases = mutableListOf(Release())
        recording.artistCredits = mutableListOf(ArtistCredit())
        recording.releases[0].artistCredits = mutableListOf(ArtistCredit())
        recording.releases[0].media = mutableListOf(Media())


        recording.title = metadata!!.allProperties["TITLE"]
        recording.releases[0].title = metadata.allProperties["ALBUM"]
        recording.artistCredits[0].name = metadata.allProperties["ARTIST"]
        recording.artistCredits[0].joinphrase = ""
        recording.releases[0].artistCredits[0].name = metadata.allProperties["ALBUMARTIST"]
        recording.releases[0].artistCredits[0].joinphrase = ""
        recording.releases[0].media!![0].trackCount = metadata.allProperties["TRACKTOTAL"]?.toInt() ?: 0
        return recording
    }

    fun createTagFields(local: AudioFile?, recordingItem: RecordingItem): Resource<List<TagField>> {
        val tagFields = HashMap<String, TagField>()
        if (local != null) {
            for (entry in local.allProperties)
                tagFields[entry.key] = TagField(entry.key, entry.value)
            tagFields.setNewValue("TITLE", recordingItem.recording_name)
            tagFields.setNewValue("MUSICBRAINZ_TRACKID", recordingItem.recording_mbid)
            tagFields.setNewValue("MUSICBRAINZ_RECORDINGID", recordingItem.recording_mbid)
            tagFields.setNewValue("MUSICBRAINZ_RELEASEID", recordingItem.release_mbid)
            tagFields.setNewValue("TRACKNUMBER", recordingItem.recording_arg)
            tagFields.setNewValue("LENGTH", recordingItem.release_mbid)
            tagFields.setNewValue("ARTIST", recordingItem.artist_credit_name)
        }
        return Resource(Resource.Status.SUCCESS, tagFields.values.toList())
    }

    private fun HashMap<String, TagField>.setNewValue(key: String, newValue: String?) {
        when {
            newValue == null -> return
            this[key] == null -> this[key] = TagField(key, newValue = newValue)
            else -> this[key]?.newValue = newValue
        }
    }
}