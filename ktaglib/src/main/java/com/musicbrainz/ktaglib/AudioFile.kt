package com.musicbrainz.ktaglib

data class AudioFile(
    var path: String?="",
    var size: Int?=0,
    var lastModified: Int?=0,
    var title: String?="",
    var albumArtist: String?="",
    var artist: String?="",
    var album: String?="",
    var track: Int?=0,
    var trackTotal: Int?=0,
    var disc: Int?=0,
    var discTotal: Int?=0,
    var duration: Int?=0,
    var year: Int?=0,
    var genre: String?=""
)
