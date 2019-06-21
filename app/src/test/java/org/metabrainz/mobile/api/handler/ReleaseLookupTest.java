package org.metabrainz.mobile.test.handler;

import org.junit.Before;
import org.junit.Test;
import org.metabrainz.mobile.api.data.obsolete.ReleaseArtist;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReleaseLookupTest extends BaseXmlParsingTestCase {

    private static final String RELEASE_LOOKUP_FIXTURE = "releaseLookup_77c85384-c846-34b1-b576-63bc00644a28.xml";
    private Release release;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RELEASE_LOOKUP_FIXTURE);
        assertNotNull(stream);
        release = new ResponseParser().parseRelease(stream);
        stream.close();
    }

    @Test
    public void testReleaseData() {
        assertEquals("77c85384-c846-34b1-b576-63bc00644a28", release.getMbid());
        assertEquals("60089b39-412b-326c-afc7-aaa47113d84f", release.getReleaseGroupMbid());
        assertEquals("781676658923", release.getBarcode());
        assertEquals("Miss Machine", release.getTitle());
        assertEquals("Official", release.getStatus());
        assertEquals("2004-08-02", release.getDate());
        assertEquals("The Dillinger Escape Plan", release.getArtists().get(0).getName());
        assertEquals("B00029J24Y", release.getAsin());
    }

    @Test
    public void testArtist() {
        ReleaseArtist artist = release.getArtists().get(0);
        assertEquals("1bc41dff-5397-4c53-bb50-469d2c277197", artist.getMbid());
        assertEquals("The Dillinger Escape Plan", artist.getName());
    }

    @Test
    public void testReleaseGroupTags() {
        assertEquals(0, release.getReleaseGroupTags().size());
    }

    @Test
    public void testReleaseGroupRatings() {
        assertEquals(4.5, release.getReleaseGroupRating(), 0.01);
        assertEquals(2, release.getReleaseGroupRatingCount());
    }

    @Test
    public void testLabelList() {
        assertEquals(1, release.getLabels().size());
    }

    @Test
    public void testTrackList() {
        assertEquals(11, release.getTrackList().size());
    }

    @Test
    public void testTrackData() {
        Track track = release.getTrackList().get(0);
        assertEquals(1, track.getPosition());
        assertEquals(147093, track.getDuration());
        assertEquals("Panasonic Youth", track.getTitle());
        assertEquals("a100efb8-0f18-44ed-8383-a503a4b7c728", track.getRecordingMbid());
    }

}
