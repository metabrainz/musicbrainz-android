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

package org.musicbrainz.android.api.handlers;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReleaseGroupSearchHandler extends DefaultHandler {
	
	private LinkedList<ReleaseGroupStub> stubs = new LinkedList<ReleaseGroupStub>();
	private ReleaseGroupStub rg;
	private ReleaseArtist releaseArtist;
	private StringBuilder sb;

	private boolean artist = false;
	
	public LinkedList<ReleaseGroupStub> getResults() {
		return stubs;
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("release-group")) {
			rg = new ReleaseGroupStub();
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
		} else if (localName.equals("release")) {
			rg.addReleaseMbid(atts.getValue("id"));
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
