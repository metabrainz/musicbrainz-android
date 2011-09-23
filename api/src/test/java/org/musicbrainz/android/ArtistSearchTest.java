package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ArtistSearchTest extends BaseXmlParsingTestCase {

	@Test
	public void testArtistSearch() throws IOException {
		
		InputStream stream = getFileStream("artistSearch_owen.xml");
		assertNotNull(stream);
		
		LinkedList<ArtistStub> artists = new ResponseParser().parseArtistSearch(stream);
		assertEquals(25, artists.size());
		
		stream.close();
	}

}
