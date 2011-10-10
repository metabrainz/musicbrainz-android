package org.musicbrainz.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ArtistLookupTest extends BaseXmlParsingTestCase {

    @Test
    public void testArtistLookup() throws IOException {

        InputStream stream = getFileStream("artistLookup_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml");
        assertNotNull(stream);

        Artist artist = new ResponseParser().parseArtist(stream);

        assertEquals("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", artist.getMbid());
        assertEquals("The Beatles", artist.getName());
        assertEquals("Group", artist.getType());
        assertEquals("GB", artist.getCountry());
        assertEquals("1957", artist.getStart());
        assertEquals("1970-04-10", artist.getEnd());

        assertEquals(34, artist.getTags().size());
        assertEquals(4.7f, artist.getRating(), 0.01);
        assertEquals(57, artist.getRatingCount());

        stream.close();
    }

}
