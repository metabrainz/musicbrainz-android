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
import java.util.LinkedList;

import org.junit.Test;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class CollectionListLookupTest extends BaseXmlParsingTestCase {

    @Test
    public void testReleaseGroupLookup() throws IOException {
        InputStream stream = getFileStream("collectionListLookup.xml");
        assertNotNull(stream);

        ResponseParser responseParser = new ResponseParser();
        LinkedList<EditorCollectionStub> collections = responseParser.parseCollectionListLookup(stream);

        assertEquals(collections.size(), 2);

        EditorCollectionStub first = collections.get(0);
        assertEquals("afc8bea7-5ffc-488d-b32f-38e71bdd9e4e", first.getMbid());
        assertEquals("Want list", first.getName());
        assertEquals("jdamcd", first.getEditor());
        assertEquals(1, first.getCount());

        EditorCollectionStub second = collections.get(1);
        assertEquals("c6f9fb72-e233-47f4-a2f6-19f16442d93a", second.getMbid());
        assertEquals("My Collection", second.getName());
        assertEquals("jdamcd", second.getEditor());
        assertEquals(2, second.getCount());

        stream.close();
    }

}
