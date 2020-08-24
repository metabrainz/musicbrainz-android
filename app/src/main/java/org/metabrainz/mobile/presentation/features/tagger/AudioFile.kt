package org.metabrainz.mobile.presentation.features.tagger

data class AudioFile(
        val path: String,
        var size: Long,
        var lastModified: Long,
        val title: String?,
        val albumArtist: String?,
        val artist: String?,
        val album: String?,
        val track: Int?,
        val trackTotal: Int?,
        val disc: Int?,
        val discTotal: Int?,
        val duration: Int?,
        val date: String?,
        val genre: String?,
        var allProperties: HashMap<String, String> = HashMap()
) {
    companion object {
        @JvmStatic
        fun getAudioFileFromHashMap(filePath: String, properties: HashMap<String, String>)
                : AudioFile {
            return AudioFile(
                    filePath,
                    properties["SIZE"]?.toLong() ?: 0,
                    properties["LAST_MODIFIED"]?.toLong() ?: 0,
                    properties["TITLE"],
                    properties["ALBUMARTIST"],
                    properties["ARTIST"],
                    properties["ALBUM"],
                    properties["TRACK"]?.toInt(),
                    properties["TRACKTOTAL"]?.toInt(),
                    properties["DISC"]?.toInt(),
                    properties["DISCTOTAL"]?.toInt(),
                    properties["DURATION"]?.toInt(),
                    properties["DATE"],
                    properties["GENRE"],
                    properties
            )
        }
    }
}