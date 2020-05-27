package com.musicbrainz.ktaglib

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
    val year: Int?,
    var genre: String?
)
