package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupSearchTest extends BaseXmlParsingTestCase {

	@Test
	public void testReleaseGroupSearch() throws IOException {
		
		InputStream stream = getFileStream("releaseGroupSearch_songs about leaving.xml");
		assertNotNull(stream);
		
		LinkedList<ReleaseGroupStub> releaseGroups = new ResponseParser().parseReleaseGroupSearch(stream);
		assertEquals(25, releaseGroups.size());

		stream.close();
	}

}
