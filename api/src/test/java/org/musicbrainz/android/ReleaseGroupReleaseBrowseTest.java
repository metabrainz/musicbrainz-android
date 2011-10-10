package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Test;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupReleaseBrowseTest extends BaseXmlParsingTestCase {

    @Test
    public void testReleaseGroupReleases() throws IOException {

        InputStream stream = getFileStream("releaseGroupReleaseBrowse_dca03435-8adb-30a5-ba82-5a162267ff38.xml");
        assertNotNull(stream);

        LinkedList<ReleaseStub> releases = new ResponseParser().parseReleaseGroupReleases(stream);
        assertEquals(14, releases.size());

        stream.close();
    }

}
