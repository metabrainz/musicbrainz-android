package org.metabrainz.mobile;

import com.google.gson.Gson;

import org.junit.Test;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import static org.metabrainz.mobile.AssertionUtils.checkArtistAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkLabelAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkRecordingAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseGroupAssertions;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtist;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabel;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecording;
import static org.metabrainz.mobile.EntityTestUtils.getTestRelease;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroup;
import static org.metabrainz.mobile.EntityTestUtils.loadResourceAsString;

public class EntityModelsTest {

    @Test
    public void testArtistModel() {
        Artist testArtist = getTestArtist();
        Artist artist = new Gson().fromJson(loadResourceAsString("artist_lookup.json"), Artist.class);
        checkArtistAssertions(testArtist, artist);
    }

    @Test
    public void testReleaseModel() {
        Release testRelease = getTestRelease();
        Release release = new Gson().fromJson(loadResourceAsString("release_lookup.json"), Release.class);
        checkReleaseAssertions(testRelease, release);
    }

    @Test
    public void testReleaseGroupModel() {
        ReleaseGroup testReleaseGroup = getTestReleaseGroup();
        ReleaseGroup releaseGroup = new Gson().fromJson(loadResourceAsString("release-group_lookup.json"), ReleaseGroup.class);
        checkReleaseGroupAssertions(testReleaseGroup, releaseGroup);
    }

    @Test
    public void testLabelModel() {
        Label testLabel = getTestLabel();
        Label label = new Gson().fromJson(loadResourceAsString("label_lookup.json"), Label.class);
        checkLabelAssertions(testLabel, label);
    }

    @Test
    public void testRecordingModel() {
        Recording testRecording = getTestRecording();
        Recording recording = new Gson().fromJson(loadResourceAsString("recording_lookup.json"), Recording.class);
        checkRecordingAssertions(testRecording, recording);
    }

}
