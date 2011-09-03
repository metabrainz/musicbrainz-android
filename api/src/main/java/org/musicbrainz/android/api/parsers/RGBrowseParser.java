package org.musicbrainz.android.api.parsers;

import java.util.ArrayList;

import org.musicbrainz.android.api.data.ReleaseGroup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RGBrowseParser extends DefaultHandler {

	private ArrayList<ReleaseGroup> results = new ArrayList<ReleaseGroup>();
	private ReleaseGroup stub;
    private StringBuilder sb;
	
	public ArrayList<ReleaseGroup> getResults() {
		return results;
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("release-group")) {
			stub = new ReleaseGroup();
			String mbid = atts.getValue("id");
			stub.setMbid(mbid); 
			
			if (atts.getValue("type") != null) {
				stub.setType(atts.getValue("type"));
			} else {
				stub.setType("unknown");
			}	
		} else if (localName.equals("title")) {
			sb = new StringBuilder();
		} else if (localName.equals("first-release-date")) {
			sb = new StringBuilder();
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		if (localName.equals("release-group")) {
			results.add(stub);
		} else if (localName.equals("title")) {
			stub.setTitle(sb.toString());
		} else if (localName.equals("first-release-date")) {
			stub.setFirstRelease(sb.toString());
		}
	}
	
	public void characters(char ch[], int start, int length) {
		
		if (sb != null) {
			for (int i=start; i<start+length; i++) {
	            sb.append(ch[i]);
	        }
		}
	}
	
}
