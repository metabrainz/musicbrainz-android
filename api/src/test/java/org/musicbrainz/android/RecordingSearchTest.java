package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class RecordingSearchTest extends BaseXmlParsingTestCase {

	@Ignore
	@Test
	public void recordingSearchTest() {
		InputStream stream = getFileStream("recordingSearch_pleaser.xml");
		assertNotNull(stream);
		
		fail("Recording searches are not supported yet.");
	}
	
}
