package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.Recording;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class RecordingLookupTest extends BaseXmlParsingTestCase {
    
    private static final String RECORDING_LOOKUP_FIXTURE = "recordingLookup_470d06f8-6c0c-443d-b521-4c4eed9f0e7e.xml";
    private Recording recording;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RECORDING_LOOKUP_FIXTURE);
        assertNotNull(stream);
        recording = new ResponseParser().parseRecording(stream);
        stream.close();
    }
	
    @Test
    public void testRecordingData() {
        assertEquals("470d06f8-6c0c-443d-b521-4c4eed9f0e7e", recording.getMbid());
        assertEquals("Sgt. Pepper’s Lonely Hearts Club Band", recording.getTitle());
        assertEquals(122000, recording.getLength());
    }
    
    @Test
    public void tstRecordingArtist() {
        ArtistNameMbid artist = recording.getArtists().get(0);
        assertEquals("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", artist.getMbid());
        assertEquals("The Beatles", artist.getName());
    }
    
    @Test
    public void testTags() {
        assertEquals(3, recording.getTags().size());
    }
    
    @Test
    public void testRatings() {
        assertEquals(59, recording.getRatingCount());
        assertEquals(4.7f, recording.getRating(), 0.01);
    }

}
