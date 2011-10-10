package org.musicbrainz.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class RatingLookupTest extends BaseXmlParsingTestCase {

    @Test
    public void testRatingLookupFromArtist() throws IOException {
        parseRatingAssertExpected("ratingLookup_artist_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml", 4.7f);
    }

    @Test
    public void testRatingLookupFromReleaseGroup() throws IOException {
        parseRatingAssertExpected("ratingLookup_release-group_bc7630eb-521a-3312-a281-adfb8c5aac7d.xml", 4f);
    }

    private void parseRatingAssertExpected(String xmlFile, float expected) throws IOException {

        InputStream stream = getFileStream(xmlFile);
        assertNotNull(stream);

        ResponseParser responseParser = new ResponseParser();
        float actual = responseParser.parseRatingLookup(stream);
        assertEquals(expected, actual, 0.01);

        stream.close();
    }

}
