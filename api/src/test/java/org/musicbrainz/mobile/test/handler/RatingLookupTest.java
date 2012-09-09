package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class RatingLookupTest extends BaseXmlParsingTestCase {

    private static final String RG_RATING_FIXTURE = "ratingLookup_release-group_bc7630eb-521a-3312-a281-adfb8c5aac7d.xml";
    private static final String ARTIST_RATING_FIXTURE = "ratingLookup_artist_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml";
    private float releaseGroupRating;
    private float artistRating;
    
    @Before
    public void doParsing() throws IOException {
        artistRating = parseRating(ARTIST_RATING_FIXTURE);
        releaseGroupRating = parseRating(RG_RATING_FIXTURE);
    }

    private float parseRating(String xmlFile) throws IOException {
        InputStream stream = getFileStream(xmlFile);
        assertNotNull(stream);
        ResponseParser responseParser = new ResponseParser();
        float rating = responseParser.parseRatingLookup(stream);
        stream.close();
        return rating;
    }
    
    @Test
    public void testArtistRatings() {
        assertEquals(artistRating, 4.7f, 0.01); 
    }

    @Test
    public void testReleaseGroupRatings() {
        assertEquals(releaseGroupRating, 4f, 0.01); 
    }
    
}
