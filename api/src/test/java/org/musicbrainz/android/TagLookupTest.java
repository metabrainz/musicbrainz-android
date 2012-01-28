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
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class TagLookupTest extends BaseXmlParsingTestCase {

    @Test
    public void testTagLookupFromArtist() throws IOException {
        parseTagsAssertExpected("tagLookup_artist_b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d.xml", 34);
    }

    @Test
    public void testTagLookupFromReleaseGroup() throws IOException {
        parseTagsAssertExpected("tagLookup_release-group_bc7630eb-521a-3312-a281-adfb8c5aac7d.xml", 7);
    }

    private void parseTagsAssertExpected(String xmlFile, int expected) throws IOException {

        InputStream stream = getFileStream(xmlFile);
        assertNotNull(stream);

        ResponseParser responseParser = new ResponseParser();
        LinkedList<Tag> tags = responseParser.parseTagLookup(stream);
        assertEquals(expected, tags.size());

        stream.close();
    }

}
