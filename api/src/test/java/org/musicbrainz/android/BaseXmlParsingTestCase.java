package org.musicbrainz.android;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.musicbrainz.android.api.ws.ResponseParser;

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
	
	protected ResponseParser getResponseParser() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		return new ResponseParser(factory);
	}
	
}
