package org.metabrainz.android.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.metabrainz.android.api.data.Artist;
import org.metabrainz.android.api.webservice.ResponseParser;

public class ArtistLookupTest extends BaseXmlParsingTestCase {
    
    private static final String ARTIST_LOOKUP_FIXTURE = "artistLookup_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml";
    private Artist artist;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(ARTIST_LOOKUP_FIXTURE);
        assertNotNull(stream);
        artist = new ResponseParser().parseArtist(stream);
        stream.close();
    }

    @Test
    public void testArtistData() {
        assertEquals("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", artist.getMbid());
        assertEquals("The Beatles", artist.getName());
        assertEquals("Group", artist.getType());
        assertEquals("GB", artist.getCountry());
        assertEquals("1957", artist.getStart());
        assertEquals("1970-04-10", artist.getEnd());
    }
    
    @Test
    public void testArtistTags() {
        assertEquals(40, artist.getTags().size());
    }
    
    @Test
    public void testArtistRatings() {
        assertEquals(4.8f, artist.getRating(), 0.01);
        assertEquals(59, artist.getRatingCount());
    }

}
