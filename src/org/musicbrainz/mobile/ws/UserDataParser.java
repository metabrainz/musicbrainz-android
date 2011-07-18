/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.ws;

import org.musicbrainz.mobile.data.UserData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser handler for user tags and ratings.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class UserDataParser extends DefaultHandler {
	
	private UserData data;
	
	private StringBuilder sb;
	
	public UserDataParser(UserData data) {
		this.data = data;
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("user-tag")) {
			sb = new StringBuilder();
		} else if (localName.equals("user-rating")) {
			sb = new StringBuilder();
		} 
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equals("user-tag")) {
			data.addTag(sb.toString());
		} else if (localName.equals("user-rating")) {
			int rating = Integer.parseInt(sb.toString());
			data.setRating(rating);
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
