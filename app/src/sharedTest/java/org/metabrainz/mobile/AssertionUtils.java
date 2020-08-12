package org.metabrainz.mobile;

import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class AssertionUtils {

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

        if (testRelease.getArtistCredits().size() != 0)
            assertThat(testRelease.getArtistCredits(), containsInAnyOrder(release.getArtistCredits().toArray()));
    }

    public static void checkArtistAssertions(Artist testArtist, Artist artist) {
        assertEquals(testArtist, artist);
        assertEquals(testArtist.getCountry(), artist.getCountry());
        assertEquals(testArtist.getDisambiguation(), artist.getDisambiguation());
        assertEquals(testArtist.getName(), artist.getName());
        assertEquals(testArtist.getSortName(), artist.getSortName());
        assertEquals(testArtist.getGender(), artist.getGender());
        assertEquals(testArtist.getType(), artist.getType());

        if (testArtist.getReleases().size() != 0)
            assertThat(testArtist.getReleases(), containsInAnyOrder(artist.getReleases().toArray()));
    }

    public static void checkReleaseGroupAssertions(ReleaseGroup testReleaseGroup, ReleaseGroup releaseGroup) {
        assertEquals(testReleaseGroup, releaseGroup);
        assertEquals(testReleaseGroup.getTitle(), releaseGroup.getTitle());
        assertEquals(testReleaseGroup.getCount(), releaseGroup.getCount());
        assertEquals(testReleaseGroup.getPrimaryType(), releaseGroup.getPrimaryType());
        assertEquals(testReleaseGroup.getFullType(), releaseGroup.getFullType());
        assertEquals(testReleaseGroup.getDisambiguation(), releaseGroup.getDisambiguation());

        if (testReleaseGroup.getArtistCredits().size() != 0)
            assertThat(testReleaseGroup.getArtistCredits(), containsInAnyOrder(releaseGroup.getArtistCredits().toArray()));
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

        if (testRecording.getArtistCredits().size() != 0)
            assertThat(testRecording.getArtistCredits(), containsInAnyOrder(recording.getArtistCredits().toArray()));
    }

    public static void checkResultItemAssertions(ResultItem testItem, ResultItem item) {
        assertEquals(testItem.getMBID(), item.getMBID());
        assertEquals(testItem.getDisambiguation(), item.getDisambiguation());
        assertEquals(testItem.getName(), item.getName());
        assertEquals(testItem.getPrimary(), item.getPrimary());
        assertEquals(testItem.getSecondary(), item.getSecondary());
    }

}
