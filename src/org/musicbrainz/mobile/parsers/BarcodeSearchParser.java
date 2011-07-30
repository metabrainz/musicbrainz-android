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

package org.musicbrainz.mobile.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser handler for barcode search. Stores the MBID of the associated
 * release if found.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class BarcodeSearchParser extends DefaultHandler {
	
	private boolean found = false; // search success
	
	private String mbid;
    
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equalsIgnoreCase("release-list")) {
			String count = atts.getValue("count");
			int c = Integer.parseInt(count); // number of results
			if (c > 0)
				found = true;
		} else if (localName.equals("release")) {
			mbid = atts.getValue("id");
		} 
	}

	/**
	 * Success status of the search.
	 * 
	 * @return boolean value representing whether a release was found from the
	 *         barcode search.
	 */
    public boolean isBarcodeFound() {
    	return found;
    }
    
    /**
     * @return Release MBID.
     */
    public String getMbid() {
    	return mbid;
    }

}
