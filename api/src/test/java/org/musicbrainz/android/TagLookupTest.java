package org.musicbrainz.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.junit.Test;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class TagLookupTest extends BaseXmlParsingTestCase {

	@Test
    public void testTagLookupFromArtist() throws IOException {
		parseTagsAssertExpected("tagLookup_artist_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml", 34);
    }
	
	@Test
	public void testTagLookupFromReleaseGroup() throws IOException {
		parseTagsAssertExpected("tagLookup_release-group_bc7630eb-521a-3312-a281-adfb8c5aac7d.xml", 7);
	}
	
	private void parseTagsAssertExpected(String xmlFile, int expected) throws IOException {
		
		InputStream stream = getFileStream(xmlFile);
		assertNotNull(stream);
		
		ResponseParser responseParser = new ResponseParser();
		Collection<String> tags = responseParser.parseTagLookup(stream);
		assertEquals(expected, tags.size());

		stream.close();
	}
	
}
