package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class LabelSearchTest extends BaseXmlParsingTestCase {
	
	@Ignore
	@Test
	public void testLabelSearch() {
		InputStream stream = getFileStream("labelSearch_count your lucky stars.xml");
		assertNotNull(stream);
		
		fail("Label searches are not supported yet");
	}

}
