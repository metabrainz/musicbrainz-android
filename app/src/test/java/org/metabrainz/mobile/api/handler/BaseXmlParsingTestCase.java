package org.metabrainz.mobile.test.handler;

import junit.framework.Assert;

import java.io.InputStream;

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
