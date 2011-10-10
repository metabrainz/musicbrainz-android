package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class ReleaseGroupLookupTest extends BaseXmlParsingTestCase {

    @Ignore
    @Test
    public void testReleaseGroupLookup() {
        InputStream stream = getFileStream("releaseGroupLookup_dca03435-8adb-30a5-ba82-5a162267ff38.xml");
        assertNotNull(stream);

        fail("Release group lookups are not supported yet.");
    }

}
