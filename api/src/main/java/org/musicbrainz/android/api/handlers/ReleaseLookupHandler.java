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

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.RecordingStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseLookupHandler extends MBHandler {
	
    private boolean inArtist = false;
    private boolean inLabel = false;
    private boolean inReleaseGroup = false;
    private boolean inTrack = false;
    private boolean inRecording = false;
    private boolean inTag = false;
	
    private Release release = new Release();
    private ReleaseArtist releaseArtist;
    private RecordingStub track;
    
    public Release getResult() {
    	return release;
    }
    
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equalsIgnoreCase("release")) {
			String releaseMbid = atts.getValue("id");
			release.setReleaseMbid(releaseMbid);
		} else if (localName.equalsIgnoreCase("title")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("status")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("barcode")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("asin")) {
		    sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("artist")) {
			inArtist = true;
			releaseArtist = new ReleaseArtist();
			releaseArtist.setMbid(atts.getValue("id"));
		} else if (localName.equalsIgnoreCase("name")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("date")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("label")) {
			inLabel = true;
		} else if (localName.equalsIgnoreCase("release-group")) {
			inReleaseGroup = true;
			String rgID = atts.getValue("id");
			release.setReleaseGroupMbid(rgID);
		} else if (localName.equalsIgnoreCase("track")) {
			inTrack = true;
			track = new RecordingStub();
		} else if (localName.equalsIgnoreCase("recording")) {
			inRecording = true;
		} else if (localName.equalsIgnoreCase("position")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("length")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("tag")) {
			inTag = true;
		} else if (localName.equalsIgnoreCase("rating")) {
			if (inReleaseGroup) {
				int ratingCount = Integer.parseInt(atts.getValue("votes-count"));
				release.setReleaseGroupRatingCount(ratingCount);
				sb = new StringBuilder();
			}
		} 
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equalsIgnoreCase("title")) {
			if (inTrack && !inRecording) {
				track.setTitle(sb.toString());
			} else if (inRecording) {
				if (track.getTitle() == null)
					track.setTitle(sb.toString());
			} else {
				release.setTitle(sb.toString());
			}
		} else if (localName.equalsIgnoreCase("status")) {
			release.setStatus(sb.toString());
		} else if (localName.equalsIgnoreCase("barcode")) {
			release.setBarcode(sb.toString());
		} else if (localName.equalsIgnoreCase("asin")) {
		    release.setAsin(sb.toString());
		} else if (localName.equalsIgnoreCase("artist")) {
			inArtist = false;
		} else if (localName.equalsIgnoreCase("name")) {
			if (inArtist && !inTag) {
				releaseArtist.setName(sb.toString());
				release.addArtist(releaseArtist);
			} else if (inLabel && !inTag) {
				release.addLabel(sb.toString());
			} else if (inReleaseGroup && inTag) {
				release.addReleaseGroupTag(sb.toString());
			}
		} else if (localName.equalsIgnoreCase("date")) {
			release.setDate(sb.toString());
		} else if (localName.equalsIgnoreCase("label")) {
			inLabel = false;
		} else if (localName.equalsIgnoreCase("release-group")) {
			inReleaseGroup = false;
		} else if (localName.equalsIgnoreCase("track")) {
			inTrack = false;
			release.addTrack(track);
		} else if (localName.equalsIgnoreCase("recording")) {
			inRecording = false;
		} else if (localName.equalsIgnoreCase("position")) {
			if (inTrack) {
			int pos = Integer.parseInt(sb.toString());
			track.setPosition(pos);
			}
		} else if (localName.equalsIgnoreCase("length")) {
			if (inTrack) {
			int dur = Integer.parseInt(sb.toString());
			track.setDuration(dur);
			}
		} else if (localName.equalsIgnoreCase("tag")) {
			inTag = false;
		} else if (localName.equalsIgnoreCase("rating")) {
			if (inReleaseGroup) {
				float rating = Float.parseFloat(sb.toString());
				release.setReleaseGroupRating(rating);
			}
		}
	}
    
}
