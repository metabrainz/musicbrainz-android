package org.metabrainz.android.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.metabrainz.android.api.data.ReleaseArtist;
import org.metabrainz.android.api.data.RecordingInfo;
import org.metabrainz.android.api.webservice.ResponseParser;

public class RecordingSearchTest extends BaseXmlParsingTestCase {
    
    private static final String RECORDING_SEARCH_FIXTURE = "recordingSearch_pleaser.xml";
    private LinkedList<RecordingInfo> recordings;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RECORDING_SEARCH_FIXTURE);
        assertNotNull(stream);
        recordings = new ResponseParser().parseRecordingSearch(stream);
        stream.close();
    }

	@Test
	public void testRecordingSearch() {
		assertEquals(25, recordings.size());
	}
	
	@Test
	public void testSearchResult() {
	    RecordingInfo searchResult = recordings.get(2);
        assertEquals("1003744a-48eb-4839-bac6-a43a2b09d09b", searchResult.getMbid());
        assertEquals("Pleaser", searchResult.getTitle());
        ReleaseArtist artist = searchResult.getArtist();
        assertEquals("a459df95-6a50-4b22-8df8-076642ce62a7", artist.getMbid());
        assertEquals("Lemuria", artist.getName());
        assertEquals(236000, searchResult.getLength());
	}
	
}
