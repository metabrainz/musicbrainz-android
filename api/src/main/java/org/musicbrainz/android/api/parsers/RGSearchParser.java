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

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser handler for release group search.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class RGSearchParser extends DefaultHandler {
	
	private LinkedList<ReleaseGroup> stubs = new LinkedList<ReleaseGroup>();
	private ReleaseGroup rg;
	private ReleaseArtist releaseArtist;
	private StringBuilder sb;

	private boolean artist = false;
	
	public LinkedList<ReleaseGroup> getResults() {
		return stubs;
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("release-group")) {
			rg = new ReleaseGroup();
			rg.setMbid(atts.getValue("id"));
			// type isn't always returned - empty string for unknown
			if (atts.getValue("type") != null)
				rg.setType(atts.getValue("type"));
			else 
				rg.setType("");
		} else if (localName.equals("title")) {
			sb = new StringBuilder();
		} else if (localName.equals("artist")) {
			artist = true;
			releaseArtist = new ReleaseArtist();
			releaseArtist.setMbid(atts.getValue("id"));
		} else if (localName.equals("name")) {
			if (artist)
				sb = new StringBuilder();
		} else if (localName.equals("release-list")) {
			int num = Integer.parseInt(atts.getValue("count"));
			rg.setNumberReleases(num);
		} else if (localName.equals("release")) {
			// only one release then store id - skip browse request
			if (rg.getNumberReleases() == 1) 
				rg.setSingleReleaseMbid(atts.getValue("id"));
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equals("release-group")) {
			stubs.add(rg);
		} else if (localName.equals("title")) {
			rg.setTitle(sb.toString());
		} else if (localName.equals("artist")) {
			artist = false;
		} else if (localName.equals("name")) {
			if (artist) {
				releaseArtist.setName(sb.toString());
				rg.addArtist(releaseArtist);
			}
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
