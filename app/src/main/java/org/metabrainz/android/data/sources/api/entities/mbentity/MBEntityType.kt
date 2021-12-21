package org.metabrainz.android.data.sources.api.entities.mbentity

import org.metabrainz.android.presentation.features.artist.ArtistActivity
import org.metabrainz.android.presentation.features.label.LabelActivity
import org.metabrainz.android.presentation.features.recording.RecordingActivity
import org.metabrainz.android.presentation.features.release.ReleaseActivity
import org.metabrainz.android.presentation.features.release_group.ReleaseGroupActivity

enum class MBEntityType(var entity: String, var typeActivityClass: Class<*>) {
    ARTIST("artist", ArtistActivity::class.java),
    RELEASE("release", ReleaseActivity::class.java),
    LABEL("label", LabelActivity::class.java),
    RELEASE_GROUP("release-group", ReleaseGroupActivity::class.java),
    RECORDING("recording", RecordingActivity::class.java),
    EVENT("event", ArtistActivity::class.java),
    INSTRUMENT("instrument", ArtistActivity::class.java);
}