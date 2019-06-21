package org.metabrainz.mobile.test.handler;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReleaseGroupBrowseTest extends BaseXmlParsingTestCase {

    private static final String RG_BROWSE_FIXTURE = "releaseGroupBrowse_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml";
    private ArrayList<ReleaseGroupSearchResult> releaseGroups;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RG_BROWSE_FIXTURE);
        assertNotNull(stream);
        releaseGroups = new ResponseParser().parseReleaseGroupBrowse(stream);
        stream.close();
    }

    @Test
    public void testReleaseGroupBrowse() {
        assertEquals(100, releaseGroups.size());
    }

}
