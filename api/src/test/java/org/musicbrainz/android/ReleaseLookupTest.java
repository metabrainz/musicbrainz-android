package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseLookupTest extends BaseXmlParsingTestCase {

	@Test
	public void testReleaseLookup() {
		InputStream stream = getFileStream("releaseLookup_2d9f9aac-1884-3939-a3b7-01437151e495.xml");
		assertNotNull(stream);
		
		try {
			Release release = new ResponseParser().parseRelease(stream);
			
			assertEquals("2d9f9aac-1884-3939-a3b7-01437151e495", release.getMbid());
			assertEquals("23355caf-a543-4b5f-80fe-449101868fc1", release.getReleaseGroupMbid());
			assertEquals("609008295007", release.getBarcode());
			assertEquals("xx", release.getTitle());
			assertEquals("Official", release.getStatus());
			assertEquals("2009-08-17", release.getDate());
			
			assertEquals(2, release.getReleaseGroupTagList().size());
			assertEquals(4, release.getReleaseGroupRating(), 0.01);
			assertEquals(2, release.getReleaseGroupRatingCount());
			
			assertEquals(1, release.getLabels().size());
			assertEquals(11, release.getTrackList().size());
			
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}

}
