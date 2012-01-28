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

package org.musicbrainz.android.api.handlers;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ArtistSearchHandler extends MBHandler {

    private LinkedList<ArtistSearchStub> results = new LinkedList<ArtistSearchStub>();
    private ArtistSearchStub stub;

    private boolean tag = false;

    public LinkedList<ArtistSearchStub> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("artist")) {
            stub = new ArtistSearchStub();
            String id = atts.getValue("id");
            stub.setMbid(id);
        } else if (localName.equals("name")) {
            if (!tag)
                sb = new StringBuilder();
        } else if (localName.equals("disambiguation")) {
            sb = new StringBuilder();
        } else if (localName.equals("tag")) {
            tag = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("artist")) {

            // check id against special purpose artist list
            boolean ignored = false;
            for (String id : Artist.SPECIAL_PURPOSE)
                if (stub.getMbid().equals(id))
                    ignored = true;

            // add result
            if (!ignored)
                results.add(stub);
        } else if (localName.equals("name")) {
            if (!tag)
                stub.setName(sb.toString());
        } else if (localName.equals("disambiguation")) {
            stub.setDisambiguation(sb.toString());
        } else if (localName.equals("tag")) {
            tag = false;
        }
    }

}
