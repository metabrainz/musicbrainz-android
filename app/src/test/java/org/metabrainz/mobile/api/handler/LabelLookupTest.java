package org.metabrainz.mobile.test.handler;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
