package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import org.metabrainz.mobile.presentation.features.artist.ArtistActivity;
import org.metabrainz.mobile.presentation.features.artist.ArtistViewModel;
import org.metabrainz.mobile.presentation.features.label.LabelActivity;
import org.metabrainz.mobile.presentation.features.label.LabelViewModel;
import org.metabrainz.mobile.presentation.features.recording.RecordingActivity;
import org.metabrainz.mobile.presentation.features.recording.RecordingViewModel;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;
import org.metabrainz.mobile.presentation.features.release.ReleaseViewModel;
import org.metabrainz.mobile.presentation.features.release_group.ReleaseGroupActivity;

public enum MBEntityType {
    ARTIST("artist", ArtistActivity.class, ArtistViewModel.class),
    RELEASE("release", ReleaseActivity.class, ReleaseViewModel.class),
    LABEL("label", LabelActivity.class, LabelViewModel.class),
    RELEASE_GROUP("release-group", ReleaseGroupActivity.class, ReleaseViewModel.class),
    RECORDING("recording", RecordingActivity.class, RecordingViewModel.class),
    EVENT("event", ArtistActivity.class, ArtistViewModel.class),
    INSTRUMENT("instrument", ArtistActivity.class, ArtistViewModel.class);

    public final String name;
    public final String display;
    public final Class typeActivityClass;
    public final Class typeViewModelClass;

    MBEntityType(String entity, Class typeActivityClass, Class typeViewModelClass) {
        this.name = entity;
        this.display = name;
        this.typeActivityClass = typeActivityClass;
        this.typeViewModelClass = typeViewModelClass;
    }
}
