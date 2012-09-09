package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ArtistSearchTest extends BaseXmlParsingTestCase {
    
    private static final String ARTIST_SEARCH_FIXTURE = "artistSearch_owen.xml";
    private LinkedList<ArtistSearchStub> artists;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(ARTIST_SEARCH_FIXTURE);
        assertNotNull(stream);
        artists = new ResponseParser().parseArtistSearch(stream);
        stream.close();
    }

    @Test
    public void testArtistSearch() {
        assertEquals(25, artists.size());
    }

}
