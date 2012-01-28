package org.musicbrainz.android;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class CollectionLookupTest extends BaseXmlParsingTestCase {
	
    @Ignore
    @Test
    public void testReleaseGroupLookup() {
        InputStream stream = getFileStream("collectionLookup_c6f9fb72-e233-47f4-a2f6-19f16442d93a.xml");
        assertNotNull(stream);

        fail("Collection lookups are not supported yet.");
    }

}
