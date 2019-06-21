package org.metabrainz.mobile.test.handler;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BarcodeSearchTest extends BaseXmlParsingTestCase {

    private static final String BARCODE_SEARCH_FIXTURE = "barcodeSearch_792258106329.xml";
    private String mbid;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(BARCODE_SEARCH_FIXTURE);
        assertNotNull(stream);
        mbid = new ResponseParser().parseMbidFromBarcode(stream);
        stream.close();
    }

    @Test
    public void testBarcodeSearch() {
        assertEquals("7fe8ee44-68bb-31da-befc-bda6da88c379", mbid);
    }

}
