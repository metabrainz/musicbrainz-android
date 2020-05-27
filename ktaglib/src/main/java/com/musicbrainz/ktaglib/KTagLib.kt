package com.musicbrainz.ktaglib

class KTagLib {

    /**
     * Retrieves an [AudioFile
     * ] representing the metadata associated with the file located at File Descriptor [fd]
     *
     * @param fd File descriptor
     * @param path Path to the file (
     */
    external fun getAudioFile(fd: Int, path: String, name: String): AudioFile?

    /**
     * Updates the file at File Descriptor [fd] with the supplied tags.
     *
     * Note: Null tags are ignored.
     *
     * @param fd File descriptor
     *
     * @return true if the tags were successfully updated.
     */
    external fun updateTags(
        fd: Int,
        title: String?,
        artist: String?,
        album: String?,
        albumArtist: String?,
        year: Int?,
        track: Int?,
        trackTotal: Int?,
        disc: Int?,
        discTotal: Int?,
        genre: String?
    ): Boolean

    /**
     * Returns a [ByteArray] representing the artwork for the file located at File Descriptor [fd], or null if no artwork can be found.
     *
     * @param fd File descriptor
     */
    external fun getArtwork(fd: Int): ByteArray?


    companion object {
        init {
            System.loadLibrary("ktaglib")
        }
    }
}
