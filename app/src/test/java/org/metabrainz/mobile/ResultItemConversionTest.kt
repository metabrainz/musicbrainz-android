package org.metabrainz.mobile;

import org.junit.Test;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.adapters.ResultItemUtils;

import static org.metabrainz.mobile.AssertionUtils.checkResultItemAssertions;
import static org.metabrainz.mobile.EntityTestUtils.getTestArtist;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabel;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecording;
import static org.metabrainz.mobile.EntityTestUtils.getTestRelease;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroup;
import static org.metabrainz.mobile.ResultItemTestUtils.getTestArtistResultItem;
import static org.metabrainz.mobile.ResultItemTestUtils.getTestLabelResultItem;
import static org.metabrainz.mobile.ResultItemTestUtils.getTestRecordingResultItem;
import static org.metabrainz.mobile.ResultItemTestUtils.getTestReleaseGroupResultItem;
import static org.metabrainz.mobile.ResultItemTestUtils.getTestReleaseResultItem;

public class ResultItemConversionTest {

    @Test
    public void testConvertArtist() {
        ResultItem testItem = getTestArtistResultItem();
        Artist artist = getTestArtist();
        ResultItem item = ResultItemUtils.getEntityAsResultItem(artist);
        checkResultItemAssertions(testItem, item);
    }

    @Test
    public void testConvertRelease() {
        ResultItem testItem = getTestReleaseResultItem();
        Release release = getTestRelease();
        ResultItem item = ResultItemUtils.getEntityAsResultItem(release);
        checkResultItemAssertions(testItem, item);
    }

    @Test
    public void testConvertLabel() {
        ResultItem testItem = getTestLabelResultItem();
        Label label = getTestLabel();
        ResultItem item = ResultItemUtils.getEntityAsResultItem(label);
        checkResultItemAssertions(testItem, item);
    }

    @Test
    public void testConvertRecording() {
        ResultItem testItem = getTestRecordingResultItem();
        Recording recording = getTestRecording();
        ResultItem item = ResultItemUtils.getEntityAsResultItem(recording);
        checkResultItemAssertions(testItem, item);
    }

    @Test
    public void testConvertReleaseGroup() {
        ResultItem testItem = getTestReleaseGroupResultItem();
        ReleaseGroup releaseGroup = getTestReleaseGroup();
        ResultItem item = ResultItemUtils.getEntityAsResultItem(releaseGroup);
        checkResultItemAssertions(testItem, item);
    }

}
