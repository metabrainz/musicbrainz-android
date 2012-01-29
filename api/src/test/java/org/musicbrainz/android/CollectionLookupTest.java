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
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class CollectionLookupTest extends BaseXmlParsingTestCase {

    @Test
    public void testReleaseGroupLookup() throws IOException {
        InputStream stream = getFileStream("collectionLookup_c6f9fb72-e233-47f4-a2f6-19f16442d93a.xml");
        assertNotNull(stream);

        ResponseParser responseParser = new ResponseParser();
        EditorCollection result = responseParser.parseCollectionLookup(stream);

        assertEquals("c6f9fb72-e233-47f4-a2f6-19f16442d93a", result.getMbid());
        assertEquals("My Collection", result.getName());
        assertEquals("jdamcd", result.getEditor());
        assertEquals(2, result.getCount());

        // TODO Test release parsing.

        stream.close();
    }

}
