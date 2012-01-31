/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.handler;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.WebLink;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ArtistLookupHandler extends MBHandler {

    private boolean inTag = false;
    private boolean inUrlRelations = false;
    private boolean inArtistRelations = false;
    private boolean inLabelRelations = false;
    private boolean inLifeSpan = false;

    private Artist artist = new Artist();
    private Tag tag;
    private WebLink link;

    public Artist getResult() {
        return artist;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("artist")) {
            if (inArtistRelations) {

            } else {
                artist.setMbid(atts.getValue("id"));
                artist.setType(atts.getValue("type"));
            }
        } else if (localName.equalsIgnoreCase("name")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("tag")) {
            inTag = true;
            sb = new StringBuilder();
            tag = new Tag();
            tag.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("rating")) {
            artist.setRatingCount(Integer.parseInt(atts.getValue("votes-count")));
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("country")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("relation")) {
            if (inUrlRelations) {
                sb = new StringBuilder();
                link = new WebLink();
                link.setType(atts.getValue("type"));
            }
        } else if (localName.equalsIgnoreCase("target")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("life-span")) {
            inLifeSpan = true;
        } else if (localName.equalsIgnoreCase("begin")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("end")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("relation-list")) {
            setRelationStatus(atts.getValue("target-type"));
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

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("name")) {
            if (inTag) {
                tag.setText(sb.toString());
                artist.addTag(tag);
            } else if (inArtistRelations) {

            } else if (inLabelRelations) {

            } else {
                artist.setName(sb.toString());
            }
        } else if (localName.equalsIgnoreCase("rating")) {
            float rating = Float.parseFloat(sb.toString());
            artist.setRating(rating);
        } else if (localName.equalsIgnoreCase("country")) {
            artist.setCountry(sb.toString());
        } else if (localName.equalsIgnoreCase("relation")) {
            if (inUrlRelations) {
                artist.addLink(link);
            }
        } else if (localName.equalsIgnoreCase("target")) {
            if (inUrlRelations) {
                link.setUrl(sb.toString());
            } else if (inArtistRelations) {

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

}