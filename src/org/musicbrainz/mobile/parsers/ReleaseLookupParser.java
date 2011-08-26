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

import org.musicbrainz.mobile.data.Release;
import org.musicbrainz.mobile.data.ReleaseArtist;
import org.musicbrainz.mobile.data.Track;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser handler for release lookup.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseLookupParser extends DefaultHandler {
	
    private Release data;
    private ReleaseArtist releaseArtist;
    private Track trk;
    private StringBuilder sb;
    
    public ReleaseLookupParser(Release data) {
        this.data = data;
    }	
	
    private boolean artist = false;
    private boolean label = false;
    private boolean releaseGroup = false;
    private boolean track = false;
    private boolean recording = false;
    private boolean tag = false;
    
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("title")) {
			sb = new StringBuilder();
		} else if (localName.equals("artist")) {
			artist = true;
			releaseArtist = new ReleaseArtist();
			releaseArtist.mbid = atts.getValue("id");
		} else if (localName.equals("name")) {
			sb = new StringBuilder();
		} else if (localName.equals("date")) {
			sb = new StringBuilder();
		} else if (localName.equals("label")) {
			label = true;
		} else if (localName.equals("release-group")) {
			releaseGroup = true;
			String rgID = atts.getValue("id");
			data.setReleaseGroupMbid(rgID);
		} else if (localName.equals("track")) {
			track = true;
			trk = new Track();
		} else if (localName.equals("recording")) {
			recording = true;
		} else if (localName.equals("position")) {
			sb = new StringBuilder();
		} else if (localName.equals("length")) {
			sb = new StringBuilder();
		} else if (localName.equals("tag")) {
			tag = true;
		} else if (localName.equals("rating")) {
			if (releaseGroup)
				sb = new StringBuilder();
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equals("title")) {
			if (!track)
				data.setTitle(sb.toString());
			else if (track && !recording)
				trk.setTitle(sb.toString());
			else if (recording) {
				// ensure recording title doesn't replace track title if specified
				if (trk.getTitle() == null)
					trk.setTitle(sb.toString());
			}
		} else if (localName.equals("artist")) {
			artist = false;
		} else if (localName.equals("name")) {
			if (artist && !tag) {
				releaseArtist.name = sb.toString();
				data.addArtist(releaseArtist);
			} else if (label && !tag) {
				data.addLabel(sb.toString());
			} else if (releaseGroup && tag) {
				data.addTag(sb.toString());
			}
		} else if (localName.equals("date")) {
			data.setDate(sb.toString());
		} else if (localName.equals("label")) {
			label = false;
		} else if (localName.equals("release-group")) {
			releaseGroup = false;
		} else if (localName.equals("track")) {
			track = false;
			data.addTrack(trk);
		} else if (localName.equals("recording")) {
			recording = false;
		} else if (localName.equals("position")) {
			if (track) {
			int pos = Integer.parseInt(sb.toString());
			trk.setPosition(pos);
			}
		} else if (localName.equals("length")) {
			if (track) {
			int dur = Integer.parseInt(sb.toString());
			trk.setDuration(dur);
			}
		} else if (localName.equals("tag")) {
			tag = false;
		} else if (localName.equals("rating")) {
			if (releaseGroup) {
				float rating = Float.parseFloat(sb.toString());
				data.setRating(rating);
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
