package org.metabrainz.mobile.test.handler;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArtistSearchTest extends BaseXmlParsingTestCase {

    private static final String ARTIST_SEARCH_FIXTURE = "artistSearch_owen.xml";
    private LinkedList<ArtistSearchResult> artists;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(ARTIST_SEARCH_FIXTURE);
        assertNotNull(stream);
        artists = new ResponseParser().parseArtistSearch(stream);
        stream.close();
    }

    @Test
    public void testArtistSearchSize() {
        assertEquals(25, artists.size());
    }

    @Test
    public void testArtistSearchName() {
        assertEquals("Owen", artists.getFirst().getName());
    }

    @Test
    public void testArtistSearchDisambiguation() {
        assertEquals("Mike Kinsella solo project", artists.getFirst().getDisambiguation());
    }

}
