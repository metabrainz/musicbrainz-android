package org.metabrainz.mobile.util

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit
import org.metabrainz.mobile.data.sources.api.entities.EntityUtils
import org.metabrainz.mobile.data.sources.api.entities.Media
import org.metabrainz.mobile.data.sources.api.entities.Track
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.presentation.features.tagger.AudioFile
import org.metabrainz.mobile.presentation.features.tagger.TagField

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

    fun getAudioFileFromTrack(track: Track?): AudioFile {
        val title = track?.title ?: ""
        val duration = track?.length?.toInt() ?: 0
        val trackNum = track?.position ?: 0
        val albumArtist = ""
        var artist: String? = ""
        var album = ""
        var trackTotal = 0
        val recording = track?.recording
        if (recording != null) {
            trackTotal = recording.trackCount
            if (recording.releases.size > 0) {
                artist = EntityUtils.getDisplayArtist(recording.releases[0].artistCredits)
                album = recording.releases[0].title!!
            }
        }
        return AudioFile("", 0, 0, title, albumArtist, artist, album,
                trackNum, trackTotal, 0, 0, duration, "", "")
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

    fun createTagFields(local: AudioFile?, track: Track?): List<TagField> {
        val tagFields = HashMap<String, TagField>()
        if (local != null) {
            for (entry in local.allProperties)
                tagFields[entry.key] = TagField(entry.key, entry.value)
            if (track != null) {
                tagFields.setNewValue("TITLE", track.title)
                tagFields.setNewValue("MUSICBRAINZ_TRACKID", track.mbid)
                tagFields.setNewValue("TRACKNUMBER", track.position.toString())
                tagFields.setNewValue("LENGTH", track.length.toString())
                tagFields.setNewValue("ARTIST",
                        EntityUtils.getDisplayArtist(track.recording?.artistCredits))
            }
        }
        return tagFields.values.toList()
    }

    private fun HashMap<String, TagField>.setNewValue(key: String, newValue: String?) {
        when {
            newValue == null -> return
            this[key] == null -> this[key] = TagField(key, newValue = newValue)
            else -> this[key]?.newValue = newValue
        }
    }
}