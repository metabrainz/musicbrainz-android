package org.metabrainz.android.data.di.brainzplayer

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.metabrainz.android.data.dao.AlbumDao
import org.metabrainz.android.data.dao.ArtistDao
import org.metabrainz.android.data.dao.PlaylistDao
import org.metabrainz.android.data.dao.SongDao
import org.metabrainz.android.data.sources.brainzplayer.AlbumEntity
import org.metabrainz.android.data.sources.brainzplayer.ArtistEntity
import org.metabrainz.android.data.sources.brainzplayer.PlaylistEntity
import org.metabrainz.android.data.sources.brainzplayer.SongEntity

@Database(
    entities = [
        SongEntity::class,
        AlbumEntity::class,
        ArtistEntity::class,
        PlaylistEntity::class
    ],
    version = 1
)
@TypeConverters(TypeConverter::class)
abstract class BrainzPlayerDatabase : RoomDatabase() {
    abstract fun songDao() : SongDao
    abstract fun albumDao() : AlbumDao
    abstract fun artistDao() : ArtistDao
    abstract fun playlistDao() : PlaylistDao
}