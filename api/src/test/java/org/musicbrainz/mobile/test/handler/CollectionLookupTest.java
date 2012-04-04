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
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class CollectionLookupTest extends BaseXmlParsingTestCase {
    
    private static final String COLLECTION_LOOKUP_FIXTURE = "collectionLookup_c6f9fb72-e233-47f4-a2f6-19f16442d93a.xml";
    private EditorCollection collection;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(COLLECTION_LOOKUP_FIXTURE);
        assertNotNull(stream);
        collection = new ResponseParser().parseCollectionLookup(stream);
        stream.close();
    }

    @Test
    public void testReleaseGroupLookup() throws IOException {
        assertEquals("c6f9fb72-e233-47f4-a2f6-19f16442d93a", collection.getMbid());
        assertEquals("My Collection", collection.getName());
        assertEquals("jdamcd", collection.getEditor());
        assertEquals(3, collection.getCount());
    }
    
    @Test
    public void testFirstRelease() throws IOException {
        ReleaseStub first = collection.getReleases().getFirst();
        assertEquals("229fef25-8e57-3465-bb92-0569b3ed1b8c", first.getReleaseMbid());
        assertEquals("Boston", first.getTitle());
        assertEquals("Boston", first.getArtists().get(0).getName());
        assertEquals("1977", first.getDate());
        assertEquals("GB", first.getCountryCode());
    }
    
    @Test
    public void testReleaseWithMultipleArtists() throws IOException {
        ReleaseStub stub = collection.getReleases().get(1);
        assertEquals("2666b0bf-0c35-492b-97e5-c932870d9c25", stub.getReleaseMbid());
        ArrayList<ArtistNameMbid> artists = stub.getArtists();
        assertEquals("Owen", artists.get(0).getName());
        assertEquals("The Rutabega", artists.get(1).getName());
        assertEquals("2004-06-08", stub.getDate());
        assertEquals("US", stub.getCountryCode());
    }
    
    @Test
    public void testLastRelease() throws IOException {
        ReleaseStub last = collection.getReleases().getLast();
        assertEquals("9f00a4d3-82b2-4084-a48b-565703b812ce", last.getReleaseMbid());
        assertEquals("New Leaves", last.getTitle());
        assertEquals("Owen", last.getArtists().get(0).getName());
        assertEquals("2009-09-22", last.getDate());
        assertEquals("US", last.getCountryCode());
    }

}
