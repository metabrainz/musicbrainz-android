package org.metabrainz.android.test.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.metabrainz.android.api.data.ReleaseGroupSearchResult;
import org.metabrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupSearchTest extends BaseXmlParsingTestCase {
    
    private static final String RG_SEARCH_FIXTURE = "releaseGroupSearch_songs about leaving.xml";
    private LinkedList<ReleaseGroupSearchResult> releaseGroups;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RG_SEARCH_FIXTURE);
        assertNotNull(stream);
        releaseGroups = new ResponseParser().parseReleaseGroupSearch(stream);
        stream.close();
    }

    @Test
    public void testReleaseGroupSearch() {
        assertEquals(25, releaseGroups.size());
    }

}
