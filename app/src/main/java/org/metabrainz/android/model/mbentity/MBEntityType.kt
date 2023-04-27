package org.metabrainz.android.model.mbentity

import org.metabrainz.android.ui.screens.artist.ArtistActivity
import org.metabrainz.android.ui.screens.label.LabelActivity
import org.metabrainz.android.ui.screens.recording.RecordingActivity
import org.metabrainz.android.ui.screens.release.ReleaseActivity
import org.metabrainz.android.ui.screens.release_group.ReleaseGroupActivity

enum class MBEntityType(var entity: String, var typeActivityClass: Class<*>) {
    ARTIST("artist", ArtistActivity::class.java),
    RELEASE("release", ReleaseActivity::class.java),
    LABEL("label", LabelActivity::class.java),
    RELEASE_GROUP("release-group", ReleaseGroupActivity::class.java),
    RECORDING("recording", RecordingActivity::class.java),
    EVENT("event", ArtistActivity::class.java),
    INSTRUMENT("instrument", ArtistActivity::class.java);
}