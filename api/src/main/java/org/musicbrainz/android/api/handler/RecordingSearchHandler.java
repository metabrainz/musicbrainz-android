/*
 * Copyright (C) 2012 Jamie McDonald
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
import org.musicbrainz.android.api.data.RecordingSearchStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecordingSearchHandler extends MBHandler {

    private LinkedList<RecordingSearchStub> results = new LinkedList<RecordingSearchStub>();
    private RecordingSearchStub stub;
    private ArtistNameMbid recordingArtist;

    private boolean inReleaseList = false;
    private boolean inArtist = false;

    public LinkedList<RecordingSearchStub> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("recording")) {
            stub = new RecordingSearchStub();
            stub.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("title") && !inReleaseList) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
            recordingArtist = new ArtistNameMbid();
            recordingArtist.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name") && inArtist) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("length")) {
            sb = new StringBuilder();
        } else if (localName.equalsIgnoreCase("release-list")) {
            inReleaseList = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("recording")) {
            results.add(stub);
        } else if (localName.equalsIgnoreCase("title") && !inReleaseList) {
            stub.setTitle(sb.toString());
        } else if (localName.equals("artist")) {
            inArtist = false;
            stub.setArtist(recordingArtist);
        } else if (localName.equalsIgnoreCase("name") && inArtist) {
            recordingArtist.setName(sb.toString());
        } else if (localName.equalsIgnoreCase("length")) {
            stub.setLength(Integer.parseInt(sb.toString()));
        } else if (localName.equalsIgnoreCase("release-list")) {
            inReleaseList = false;
        }
    }

}
