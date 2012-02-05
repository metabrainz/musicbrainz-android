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

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.EditorCollection;
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
        assertEquals(2, collection.getCount());
    }

}
