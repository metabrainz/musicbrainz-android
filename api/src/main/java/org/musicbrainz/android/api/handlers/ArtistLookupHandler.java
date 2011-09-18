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

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.WebLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArtistLookupHandler extends DefaultHandler {

    private boolean inTag = false;
    private boolean inUrlRelations = false;
    private boolean inArtistRelations = false;
    private boolean inLabelRelations = false;
    private boolean inLifeSpan = false;
    
    private Artist artist = new Artist();
    private WebLink link;
    private StringBuilder sb;
    
    public Artist getResult() {
    	return artist;
    }
    
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equalsIgnoreCase("artist")) {
			if (inArtistRelations) {
				
			} else {
				String mbid = atts.getValue("id");
				artist.setMbid(mbid);
				String type = atts.getValue("type");
				artist.setType(type);
			}
		} else if (localName.equalsIgnoreCase("name")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("tag")) {
			inTag = true;
		} else if (localName.equalsIgnoreCase("rating")) {
			int ratingCount = Integer.parseInt(atts.getValue("votes-count"));
			artist.setRatingCount(ratingCount);
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("country")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("relation")) {
			if (inUrlRelations) {
				link = new WebLink();
				link.setType(atts.getValue("type"));
			}
		} else if (localName.equalsIgnoreCase("target")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("tag")) {
			inTag = true;
		} else if (localName.equalsIgnoreCase("life-span")) {
			inLifeSpan = true;
		} else if (localName.equalsIgnoreCase("begin")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("end")) {
			sb = new StringBuilder();
		} else if (localName.equalsIgnoreCase("relation-list")) {
			String type = atts.getValue("target-type");
			setRelationStatus(type);
		} 
	}

	private void setRelationStatus(String type) {
		if (type.equalsIgnoreCase("url")) {
			inUrlRelations = true;
		} else if (type.equalsIgnoreCase("artist")) {
			inArtistRelations = true;
		} else if (type.equalsIgnoreCase("label")) {
			inLabelRelations = true;
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		
		if (localName.equalsIgnoreCase("name")) {
			if (inTag) {
				artist.addTag(sb.toString());
			} else if (inArtistRelations) {
			
			} else if (inLabelRelations) {
				
			} else{
				artist.setName(sb.toString());
			}
		} else if (localName.equalsIgnoreCase("rating")) {
			float rating = Float.parseFloat(sb.toString());
			artist.setRating(rating);
		} else if (localName.equalsIgnoreCase("country")) {
			artist.setCountry(sb.toString());
		} else if (localName.equalsIgnoreCase("relation")) {
			if (inUrlRelations){
				artist.addLink(link);
			}
		} else if (localName.equalsIgnoreCase("target")) {
			if (inUrlRelations) {
				link.setUrl(sb.toString());
			} else if (inArtistRelations){
				
			} else if (inLabelRelations) {
				
			}
		} else if (localName.equalsIgnoreCase("tag")) {
			inTag = false;
		} else if (localName.equalsIgnoreCase("life-span")) {
			inLifeSpan = false;
		} else if (localName.equalsIgnoreCase("begin")) {
			if (inLifeSpan) {
				artist.setBegin(sb.toString());
			}
		} else if (localName.equalsIgnoreCase("end")) {
			if (inLifeSpan) {
				artist.setEnd(sb.toString());
			}
		} else if (localName.equalsIgnoreCase("relation")) {
			inUrlRelations = false;
			inArtistRelations = false;
			inLabelRelations = false;
		}
	}
	
	public void characters(char ch[], int start, int length) {
		
		if (sb != null) {
			for (int i = start; i < start + length; i++) {
	            sb.append(ch[i]);
	        }
		}
	}

}