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

package org.musicbrainz.android.api.parsers;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.WebLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser handler for artist lookup.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ArtistLookupParser extends DefaultHandler {
	
    private boolean releaseGroup = false;
    private boolean tag = false;
    
    private Artist data = new Artist();
    private WebLink link;
    
    private StringBuilder sb;
    
    public Artist getResult() {
    	return data;
    }
    
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("artist")) {
			String mbid = atts.getValue("id");
			data.setMbid(mbid);
		} else if (localName.equals("name")) {
			sb = new StringBuilder();
		} else if (localName.equals("tag")) {
			tag = true;
		} else if (localName.equals("rating")) {
			if (!releaseGroup) {
				sb = new StringBuilder();
			}
		} else if (localName.equals("relation")) {
			link = new WebLink();
			link.setType(atts.getValue("type"));
		} else if (localName.equals("target")) {
			sb = new StringBuilder();
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equals("name")) {
			if (tag) {
				data.addTag(sb.toString());
			}else {
				data.setName(sb.toString());
			}
		} else if (localName.equals("tag")) {
			tag = false;
		} else if (localName.equals("rating")) {
			if (!releaseGroup) {
				float rating = Float.parseFloat(sb.toString());
				data.setRating(rating);
			}
		} else if (localName.equals("release-group")) {
			releaseGroup = false;
		} else if (localName.equals("relation")) {
			data.addLink(link);
		} else if (localName.equals("target")) {
			link.setUrl(sb.toString());
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