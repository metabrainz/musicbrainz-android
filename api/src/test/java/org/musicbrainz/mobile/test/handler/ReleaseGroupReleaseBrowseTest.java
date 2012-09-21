package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupReleaseBrowseTest extends BaseXmlParsingTestCase {
    
    private static final String RG_RELEASE_BROWSE_FIXTURE = "releaseGroupReleaseBrowse_dca03435-8adb-30a5-ba82-5a162267ff38.xml";
    private LinkedList<ReleaseStub> releases;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RG_RELEASE_BROWSE_FIXTURE);
        assertNotNull(stream);
        releases = new ResponseParser().parseReleaseGroupReleases(stream);
        stream.close();
    }

    @Test
    public void testReleaseGroupReleases() {
        assertEquals(14, releases.size());
    }
    
    @Test
    public void testArtistSortName() {
        ReleaseStub stub = releases.getFirst();
        ArtistNameMbid artist = stub.getArtists().get(0);
        assertEquals("Beatles, The", artist.getSortName());
    }

}
