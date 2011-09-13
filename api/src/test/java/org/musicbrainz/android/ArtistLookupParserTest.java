package org.musicbrainz.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.ws.ResponseParser;

public class ArtistLookupParserTest extends BaseXmlParsingTestCase {
	
	@Test
	public void testArtistLookup() {
		InputStream stream = getFileStream("artistLookup_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml");
		assertNotNull(stream);
		
		ResponseParser responseParser = new ResponseParser();
		try {
			Artist artist = responseParser.parseArtist(stream);
			
			assertEquals("The Beatles", artist.getName());
//			assertEquals("Group", artist.getType());
//			
//			assertEquals("GB", artist.getCountry());
//			
//			assertEquals("1957", artist.getStart());
//			assertEquals("1970-04-10", artist.getEnd());
			
			assertEquals(34, artist.getTagList().size());
			assertEquals(4.7f, artist.getRating(), 0.01);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
