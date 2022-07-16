package org.metabrainz.android.data.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import org.metabrainz.android.data.sources.brainzplayer.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepositoryImpl @Inject constructor(private val context: Context): SongRepository {

    private val songs = mutableListOf<Song>()

    override suspend fun fetchSongs(): List<Song> {
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        val songProjection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.SIZE
        )
        val sortOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val songQuery = context.contentResolver.query(
            collection,
            songProjection,
            isMusic,
            null,
            sortOrder
        )
        songQuery?.use { cursor ->
            val id = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val name = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val albumId = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)
            while (cursor.moveToNext()) {
                val songId = cursor.getLong(id).toString()
                val albumID = cursor.getLong(albumId).toString()
                val songName = cursor.getString(name)
                val artistName = cursor.getString(artist)
                val albumArt = "content://media/external/audio/albumart/$albumID"
                val contentUri = ContentUris.withAppendedId(
                    collection,
                    songId.toLong()
                ).toString()
                songs += Song(songId, songName, artistName, contentUri, albumArt)
            }
        }
        return songs
    }
}