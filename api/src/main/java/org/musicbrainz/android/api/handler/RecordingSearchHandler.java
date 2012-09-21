package org.musicbrainz.android.api.handler;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ArtistNameMbid;
import org.musicbrainz.android.api.data.RecordingStub;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecordingSearchHandler extends MBHandler {

    private LinkedList<RecordingStub> results = new LinkedList<RecordingStub>();
    private RecordingStub stub;
    private ArtistNameMbid recordingArtist;

    private boolean inReleaseList;
    private boolean inArtist;

    public LinkedList<RecordingStub> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("recording")) {
            stub = new RecordingStub();
            stub.setMbid(atts.getValue("id"));
        } else if (localName.equals("title") && !inReleaseList) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            recordingArtist = new ArtistNameMbid();
            recordingArtist.setMbid(atts.getValue("id"));
        } else if (localName.equals("name") && inArtist) {
            buildString();
        } else if (localName.equals("length")) {
            buildString();
        } else if (localName.equals("release-list")) {
            inReleaseList = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("recording")) {
            results.add(stub);
        } else if (localName.equals("title") && !inReleaseList) {
            stub.setTitle(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
            stub.setArtist(recordingArtist);
        } else if (localName.equals("name") && inArtist) {
            recordingArtist.setName(getString());
        } else if (localName.equals("length")) {
            stub.setLength(Integer.parseInt(getString()));
        } else if (localName.equals("release-list")) {
            inReleaseList = false;
        }
    }

}
