package org.musicbrainz.android;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class CollectionListLookupTest extends BaseXmlParsingTestCase {

    @Ignore
    @Test
    public void testReleaseGroupLookup() {
        InputStream stream = getFileStream("collectionListLookup.xml");
        assertNotNull(stream);

        fail("Collection list lookups are not supported yet.");
    }
	
}
