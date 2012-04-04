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
import org.musicbrainz.android.api.data.ReleaseStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ReleaseStubHandler extends MBHandler {

    private LinkedList<ReleaseStub> results = new LinkedList<ReleaseStub>();
    private ReleaseStub stub;
    private ArtistNameMbid releaseArtist;

    private boolean inArtist;
    private boolean inLabel;

    public LinkedList<ReleaseStub> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("release")) {
            stub = new ReleaseStub();
            stub.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equals("title")) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            releaseArtist = new ArtistNameMbid();
            releaseArtist.setMbid(atts.getValue("id"));
        } else if (localName.equals("name")) {
            buildString();
        } else if (localName.equals("date")) {
            buildString();
        } else if (localName.equals("country")) {
            buildString();
        } else if (localName.equals("track-list")) {
            String num = atts.getValue("count");
            int tracks = Integer.parseInt(num);
            stub.setTracksNum(stub.getTracksNum() + tracks);
        } else if (localName.equals("label")) {
            inLabel = true;
        } else if (localName.equals("format")) {
            buildString();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("release")) {
            results.add(stub);
        } else if (localName.equals("title")) {
            stub.setTitle(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
        } else if (localName.equals("name") && inArtist) {
            releaseArtist.setName(getString());
            stub.addArtist(releaseArtist);
        } else if (localName.equals("name") && inLabel) {
            stub.addLabel(getString());
        } else if (localName.equals("date")) {
            stub.setDate(getString());
        } else if (localName.equals("country")) {
            stub.setCountryCode(getString().toUpperCase());
        } else if (localName.equals("label")) {
            inLabel = false;
        } else if (localName.equals("format")) {
            stub.addFormat(getString());
        }
    }

}
