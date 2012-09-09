package org.musicbrainz.mobile.test.handler;

import java.io.InputStream;

import junit.framework.Assert;

public abstract class BaseXmlParsingTestCase {

    protected InputStream getFileStream(String fileName) {
        try {
            return getClass().getResourceAsStream("/" + fileName);
        } catch (Exception e) {
            Assert.fail("Problem loading file : " + fileName + " " + e.getMessage());
            throw new RuntimeException();
        }
    }

}
