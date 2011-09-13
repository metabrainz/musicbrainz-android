package org.musicbrainz.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.junit.Test;
import org.musicbrainz.android.api.ws.ResponseParser;

public class TagLookupParserTest extends BaseXmlParsingTestCase {

	@Test
    public void testTagLookupFromArtist() {
		parseTagsAssertExpected("tagLookup_artist_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml", 34);
    }
	
	@Test
	public void testTagLookupFromReleaseGroup() {
		parseTagsAssertExpected("tagLookup_release-group_bc7630eb-521a-3312-a281-adfb8c5aac7d.xml", 7);
	}
	
	private void parseTagsAssertExpected(String xmlFile, int expected) {
		InputStream stream = getFileStream(xmlFile);
		assertNotNull(stream);
		
		try {
			ResponseParser responseParser = new ResponseParser();
			Collection<String> tags = responseParser.parseTagLookup(stream);
			assertEquals(expected, tags.size());
			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
