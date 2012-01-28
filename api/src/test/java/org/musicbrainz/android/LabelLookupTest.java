package org.musicbrainz.android;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class LabelLookupTest extends BaseXmlParsingTestCase {

    @Ignore
    @Test
    public void testReleaseGroupLookup() {
        InputStream stream = getFileStream("labelLookup_a4f904e0-f048-4c13-88ec-f9f31f3e6109.xml");
        assertNotNull(stream);

        fail("Label lookups are not supported yet.");
    }
	
}
