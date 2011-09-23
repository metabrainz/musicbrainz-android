package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class BarcodeSearchTest extends BaseXmlParsingTestCase {

	@Test
	public void testBarcodeSearch() throws IOException {
		
		InputStream stream = getFileStream("barcodeSearch_792258106329.xml");
		assertNotNull(stream);
		
		String mbid = new ResponseParser().parseMbidFromBarcode(stream);
		assertEquals("7fe8ee44-68bb-31da-befc-bda6da88c379", mbid);

		stream.close();
	}

}
