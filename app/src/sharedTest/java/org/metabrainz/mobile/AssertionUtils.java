package org.metabrainz.mobile;

import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import static org.junit.Assert.assertEquals;

public class
AssertionUtils {

    public static void checkWikiAssertions(WikiSummary testSummary, WikiSummary summary) {
        assertEquals(testSummary.getExtract(), summary.getExtract());
    }

    public static void checkReleaseAssertions(Release testRelease, Release release) {
        assertEquals(testRelease, release);
        assertEquals(testRelease.getTitle(), release.getTitle());
        assertEquals(testRelease.getBarcode(), release.getBarcode());
        assertEquals(testRelease.getStatus(), release.getStatus());
        assertEquals(testRelease.getCountry(), release.getCountry());
        assertEquals(testRelease.getDisambiguation(), release.getDisambiguation());
        assertEquals(testRelease.getDate(), release.getDate());
    }

    public static void checkArtistAssertions(Artist testArtist, Artist artist) {
        assertEquals(artist, testArtist);
        assertEquals(testArtist.getCountry(), artist.getCountry());
        assertEquals(testArtist.getDisambiguation(), artist.getDisambiguation());
        assertEquals(testArtist.getName(), artist.getName());
        assertEquals(testArtist.getSortName(), artist.getSortName());
        assertEquals(testArtist.getGender(), artist.getGender());
    }

    public static void checkReleaseGroupAssertions(ReleaseGroup testReleaseGroup, ReleaseGroup releaseGroup) {
        assertEquals(testReleaseGroup, releaseGroup);
        assertEquals(testReleaseGroup.getTitle(), releaseGroup.getTitle());
        assertEquals(testReleaseGroup.getCount(), releaseGroup.getCount());
        assertEquals(testReleaseGroup.getPrimaryType(), releaseGroup.getPrimaryType());
        assertEquals(testReleaseGroup.getFullType(), releaseGroup.getFullType());
        assertEquals(testReleaseGroup.getDisambiguation(), releaseGroup.getDisambiguation());
    }

    public static void checkLabelAssertions(Label testLabel, Label label) {
        assertEquals(testLabel, label);
        assertEquals(testLabel.getCode(), label.getCode());
        assertEquals(testLabel.getType(), label.getType());
        assertEquals(testLabel.getCountry(), label.getCountry());
    }

    public static void checkRecordingAssertions(Recording testRecording, Recording recording) {
        assertEquals(testRecording, recording);
        assertEquals(testRecording.getTitle(), testRecording.getTitle());
        assertEquals(testRecording.getDuration(), recording.getDuration());
        assertEquals(testRecording.getLength(), recording.getLength());
        assertEquals(testRecording.getTrackCount(), recording.getTrackCount());
        assertEquals(testRecording.getDisambiguation(), recording.getDisambiguation());
    }

}
