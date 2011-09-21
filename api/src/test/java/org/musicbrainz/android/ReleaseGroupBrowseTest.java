package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupBrowseTest extends BaseXmlParsingTestCase {

	@Test
	public void testReleaseGroupBrowse() {
		InputStream stream = getFileStream("releaseGroupBrowse_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml");
		assertNotNull(stream);
		
		try {
			ArrayList<ReleaseGroupStub> releaseGroups = new ResponseParser().parseReleaseGroupBrowse(stream);
			assertEquals(100, releaseGroups.size());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
