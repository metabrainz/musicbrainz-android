package org.metabrainz.mobile.test.handler;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LabelSearchTest extends BaseXmlParsingTestCase {

    private static final String LABEL_SEARCH_FIXTURE = "labelSearch_count your lucky stars.xml";
    private LinkedList<LabelSearchResult> labels;

    @Before
    public void doParsing() throws IOException {
        InputStream stream = getFileStream(LABEL_SEARCH_FIXTURE);
        assertNotNull(stream);
        labels = new ResponseParser().parseLabelSearch(stream);
        stream.close();
    }

    @Test
    public void testLabelSearch() {
        assertEquals(25, labels.size());
    }

}
