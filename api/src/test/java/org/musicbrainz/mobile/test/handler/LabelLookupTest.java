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
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.musicbrainz.android.api.data.Label;
import org.musicbrainz.android.api.webservice.ResponseParser;

public class LabelLookupTest extends BaseXmlParsingTestCase {
    
    private static final String LABEL_LOOKUP_FIXTURE = "labelLookup_a4f904e0-f048-4c13-88ec-f9f31f3e6109.xml";
    private Label label;
    
    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(LABEL_LOOKUP_FIXTURE);
        assertNotNull(stream);
        label = new ResponseParser().parseLabel(stream);
        stream.close();
    }

    @Test
    public void testLabelData() {
        assertEquals("a4f904e0-f048-4c13-88ec-f9f31f3e6109", label.getMbid());
        assertEquals("Barsuk Records", label.getName());
        assertEquals("US", label.getCountry());
        assertEquals("1994", label.getStart());
        assertNull(label.getEnd());
    }
    
    @Test
    public void testTags() {
        assertEquals(4, label.getTags().size());
    }
    
    @Test
    public void testRatings() {
        assertEquals(1, label.getRatingCount());
        assertEquals(5f, label.getRating(), 0.01);
    }
    
    @Test
    public void testLinks() {
        assertEquals(4, label.getLinks().size());
    }
	
}
