package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseInfo;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupReleaseBrowseTest extends BaseXmlParsingTestCase {
    
    private static final String RG_RELEASE_BROWSE_FIXTURE = "releaseGroupReleaseBrowse_dca03435-8adb-30a5-ba82-5a162267ff38.xml";
    private LinkedList<ReleaseInfo> releases;

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
    public void testBasicReleaseInfo() {
        ReleaseInfo release = releases.getFirst();
        assertEquals("Rubber Soul", release.getTitle());
        assertEquals("2c54468a-dedf-4ac9-a358-562c4c6c5dd7", release.getReleaseMbid());
        assertEquals(14, release.getTracksNum());
        assertEquals("GB", release.getCountryCode());
        assertEquals("1965-12-03", release.getDate());
    }
    
    @Test
    public void testArtistSortName() {
        ReleaseInfo release = releases.getFirst();
        ReleaseArtist artist = release.getArtists().get(0);
        assertEquals("Beatles, The", artist.getSortName());
    }

}
