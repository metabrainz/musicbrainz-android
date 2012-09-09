package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class TagLookupTest extends BaseXmlParsingTestCase {
    
    private static final String ARTIST_TAG_FIXTURE = "tagLookup_artist_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml";
    private static final String RG_TAG_FIXTURE = "tagLookup_release-group_bc7630eb-521a-3312-a281-adfb8c5aac7d.xml";
    private LinkedList<Tag> artistTags;
    private LinkedList<Tag> releaseGroupTags;
    
    @Before
    public void doParsing() throws IOException {
        artistTags = parseTags(ARTIST_TAG_FIXTURE);
        releaseGroupTags = parseTags(RG_TAG_FIXTURE);
    }
    
    private LinkedList<Tag> parseTags(String xmlFile) throws IOException {
        InputStream stream = getFileStream(xmlFile);
        assertNotNull(stream);
        LinkedList<Tag> tags = new ResponseParser().parseTagLookup(stream);
        stream.close();
        return tags;
    }

    @Test
    public void testArtistTags() {
        assertEquals(artistTags.size(), 34);
    }

    @Test
    public void testReleaseGroupTags() {
        assertEquals(releaseGroupTags.size(), 7);
    }

}
