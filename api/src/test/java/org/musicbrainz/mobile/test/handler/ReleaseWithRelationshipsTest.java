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

package org.musicbrainz.mobile.test.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseWithRelationshipsTest extends BaseXmlParsingTestCase {
    
    private static final String RELEASE_LOOKUP_FIXTURE = "releaseLookup_ade577f6-6087-4a4f-8e87-38b0f8169814.xml";
    private Release release;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(RELEASE_LOOKUP_FIXTURE);
        assertNotNull(stream);
        release = new ResponseParser().parseRelease(stream);
        stream.close();
    }
    
    @Test
    public void testArtistName() {
        assertEquals(1, release.getArtists().size());
        assertEquals("The Beatles", release.getArtists().get(0).getName());
    }

}
