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

import java.util.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser handler for refreshing tags and ratings.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class TagRatingParser extends DefaultHandler {
	
	private Collection<String> tags;
	private float ratingValue;
	
	private StringBuilder sb;
	
	public TagRatingParser() {
		// no parameter constructor for ratings
	}
	
	public TagRatingParser(Collection<String> tags) {
		this.tags = tags;
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("tag")) {
			sb = new StringBuilder();
		} else if (localName.equals("rating")) {
			sb = new StringBuilder();
		} 
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equals("tag")) {
			tags.add(sb.toString());
		} else if (localName.equals("rating")) {
			float rating = Float.parseFloat(sb.toString());
			ratingValue = rating;
		} 
	}
	
	public void characters(char ch[], int start, int length) {
		
		if (sb != null) {
			for (int i=start; i<start+length; i++) {
	            sb.append(ch[i]);
	        }
		}
	}
	
	public float getRating() {
		return ratingValue;
	}

}
