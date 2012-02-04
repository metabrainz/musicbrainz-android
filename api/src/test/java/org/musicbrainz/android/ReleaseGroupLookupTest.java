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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ReleaseGroupLookupTest extends BaseXmlParsingTestCase {

    @Test
    public void testReleaseGroupLookup() throws IOException {
        InputStream stream = getFileStream("releaseGroupLookup_60089b39-412b-326c-afc7-aaa47113d84f.xml");
        assertNotNull(stream);

        ReleaseGroup rg = new ResponseParser().parseReleaseGroupLookup(stream);
        
        assertEquals("60089b39-412b-326c-afc7-aaa47113d84f", rg.getMbid());
        assertEquals("Miss Machine", rg.getTitle());
        assertEquals("Album", rg.getType());
        assertEquals("2004", rg.getReleaseYear());
        
        ArtistNameMbid artist = rg.getArtists().get(0);
        assertEquals("The Dillinger Escape Plan", artist.getName());
        assertEquals("1bc41dff-5397-4c53-bb50-469d2c277197", artist.getMbid());
        
        assertEquals(2, rg.getRatingCount());
        assertEquals(4.5f, rg.getRating(), 0.01);
        assertEquals(2, rg.getTags().size());
        assertEquals(2, rg.getLinks().size());

        stream.close();
    }

}
