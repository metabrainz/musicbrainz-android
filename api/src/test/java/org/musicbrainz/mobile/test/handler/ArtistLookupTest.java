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
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class ArtistLookupTest extends BaseXmlParsingTestCase {
    
    private static final String ARTIST_LOOKUP_FIXTURE = "artistLookup_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml";
    private Artist artist;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(ARTIST_LOOKUP_FIXTURE);
        assertNotNull(stream);
        artist = new ResponseParser().parseArtist(stream);
        stream.close();
    }

    @Test
    public void testArtistData() {
        assertEquals("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", artist.getMbid());
        assertEquals("The Beatles", artist.getName());
        assertEquals("Group", artist.getType());
        assertEquals("GB", artist.getCountry());
        assertEquals("1957", artist.getStart());
        assertEquals("1970-04-10", artist.getEnd());
    }
    
    @Test
    public void testArtistTags() {
        assertEquals(34, artist.getTags().size());
    }
    
    @Test
    public void testArtistRatings() {
        assertEquals(4.7f, artist.getRating(), 0.01);
        assertEquals(57, artist.getRatingCount());
    }

}
