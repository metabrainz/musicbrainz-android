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

import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CollectionHandler extends MBHandler {

    private EditorCollection collection = new EditorCollection();
    private ReleaseStub stub;

    private boolean inArtist;

    public EditorCollection getCollection() {
        return collection;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equalsIgnoreCase("collection")) {
            collection.setMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("name")) {
            buildString();
        } else if (localName.equalsIgnoreCase("editor")) {
            buildString();
        } else if (localName.equalsIgnoreCase("release-list")) {
            collection.setCount(Integer.parseInt(atts.getValue("count")));
        } else if (localName.equalsIgnoreCase("release")) {
            stub = new ReleaseStub();
            stub.setReleaseMbid(atts.getValue("id"));
        } else if (localName.equalsIgnoreCase("title")) {
            buildString();
        } else if (localName.equalsIgnoreCase("date")) {
            buildString();
        } else if (localName.equalsIgnoreCase("country")) {
            buildString();
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("name") && !inArtist) {
            collection.setName(getString());
        } else if (localName.equalsIgnoreCase("name")) {
            stub.setArtistName(getString());
        } else if (localName.equalsIgnoreCase("editor")) {
            collection.setEditor(getString());
        } else if (localName.equalsIgnoreCase("release")) {
            collection.addRelease(stub);
        } else if (localName.equalsIgnoreCase("title")) {
            stub.setTitle(getString());
        } else if (localName.equalsIgnoreCase("date")) {
            stub.setDate(getString());
        } else if (localName.equalsIgnoreCase("country")) {
            stub.setCountryCode(getString());
        } else if (localName.equalsIgnoreCase("artist")) {
            inArtist = false;
        }
    }

}
