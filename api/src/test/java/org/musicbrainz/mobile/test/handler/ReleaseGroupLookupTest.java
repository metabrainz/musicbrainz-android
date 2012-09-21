package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupLookupTest extends BaseXmlParsingTestCase {
    
    private static final String RG_LOOKUP_FIXTURE = "releaseGroupLookup_60089b39-412b-326c-afc7-aaa47113d84f.xml";
    private ReleaseGroup releaseGroup;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RG_LOOKUP_FIXTURE);
        assertNotNull(stream);
        releaseGroup = new ResponseParser().parseReleaseGroupLookup(stream);
        stream.close();
    }
    
    @Test
    public void testReleaseGroupLookup() {
        assertEquals("60089b39-412b-326c-afc7-aaa47113d84f", releaseGroup.getMbid());
        assertEquals("Miss Machine", releaseGroup.getTitle());
        assertEquals("Album", releaseGroup.getType());
        assertEquals("2004", releaseGroup.getReleaseYear());
    }
    
    @Test
    public void testReleaseGroupArtist() {
        ReleaseArtist artist = releaseGroup.getArtists().get(0);
        assertEquals("The Dillinger Escape Plan", artist.getName());
        assertEquals("1bc41dff-5397-4c53-bb50-469d2c277197", artist.getMbid());
    }
    
    @Test
    public void testTags() {
        assertEquals(2, releaseGroup.getTags().size());
    }
    
    @Test
    public void testRatings() {
        assertEquals(2, releaseGroup.getRatingCount());
        assertEquals(4.5f, releaseGroup.getRating(), 0.01);
    }
    
    @Test
    public void testLinks() {
        assertEquals(2, releaseGroup.getLinks().size());
    }

}
