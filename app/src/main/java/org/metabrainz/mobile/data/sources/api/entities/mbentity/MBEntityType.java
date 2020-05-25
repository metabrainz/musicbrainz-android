package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import org.metabrainz.mobile.presentation.features.artist.ArtistActivity;
import org.metabrainz.mobile.presentation.features.label.LabelActivity;
import org.metabrainz.mobile.presentation.features.recording.RecordingActivity;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;
import org.metabrainz.mobile.presentation.features.release_group.ReleaseGroupActivity;

public enum MBEntityType {
    ARTIST("artist", ArtistActivity.class),
    RELEASE("release", ReleaseActivity.class),
    LABEL("label", LabelActivity.class),
    RELEASE_GROUP("release-group", ReleaseGroupActivity.class),
    RECORDING("recording", RecordingActivity.class),
    EVENT("event", ArtistActivity.class),
    INSTRUMENT("instrument", ArtistActivity.class);

    public final String name;
    public final Class typeActivityClass;

    MBEntityType(String entity, Class typeActivityClass) {
        this.name = entity;
        this.typeActivityClass = typeActivityClass;
    }
}
