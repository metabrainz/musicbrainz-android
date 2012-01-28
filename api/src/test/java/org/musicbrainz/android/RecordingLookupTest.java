package org.musicbrainz.android;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class RecordingLookupTest extends BaseXmlParsingTestCase {
	
    @Ignore
    @Test
    public void testReleaseGroupLookup() {
        InputStream stream = getFileStream("recordingLookup_1003744a-48eb-4839-bac6-a43a2b09d09b.xml");
        assertNotNull(stream);

        fail("Recording lookups are not supported yet.");
    }

}
