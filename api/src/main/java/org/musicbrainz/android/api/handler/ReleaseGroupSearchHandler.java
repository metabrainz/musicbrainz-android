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

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseGroupSearchHandler extends MBHandler {

    private LinkedList<ReleaseGroupStub> stubs = new LinkedList<ReleaseGroupStub>();
    private ReleaseGroupStub rg;
    private ArtistNameMbid releaseArtist;

    private boolean inArtist;

    public LinkedList<ReleaseGroupStub> getResults() {
        return stubs;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("release-group")) {
            rg = new ReleaseGroupStub();
            rg.setMbid(atts.getValue("id"));
            rg.setType(atts.getValue("type"));
        } else if (localName.equalsIgnoreCase("title")) {
            buildString();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name") && inArtist) {
            buildString();
        } else if (localName.equalsIgnoreCase("release")) {
            rg.addReleaseMbid(atts.getValue("id"));
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("release-group")) {
            stubs.add(rg);
        } else if (localName.equalsIgnoreCase("title")) {
            rg.setTitle(getString());
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = false;
        } else if (localName.equalsIgnoreCase("name") && inArtist) {
            releaseArtist.setName(getString());
            rg.addArtist(releaseArtist);
        }
    }

}
