/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseSearchTest extends BaseXmlParsingTestCase {
    
    private static final String RELEASE_SEARCH_FIXTURE = "releaseSearch_songs about leaving.xml";
    private LinkedList<ReleaseStub> releases;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RELEASE_SEARCH_FIXTURE);
        assertNotNull(stream);
        releases = new ResponseParser().parseReleaseSearch(stream);
        stream.close();
    }

    @Test
    public void testReleaseSearch() {
        assertEquals(25, releases.size());
    }

}
