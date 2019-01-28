package org.metabrainz.android.api.handler;

import java.util.LinkedList;

import org.metabrainz.android.api.data.ReleaseArtist;
import org.metabrainz.android.api.data.RecordingSearchResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecordingSearchHandler extends MBHandler {

    private LinkedList<RecordingSearchResult> results = new LinkedList<RecordingSearchResult>();
    private RecordingSearchResult recording;
    private ReleaseArtist recordingArtist;

    private boolean inReleaseList;
    private boolean inArtist;

    public LinkedList<RecordingSearchResult> getResults() {
        return results;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (localName.equals("recording")) {
            recording = new RecordingSearchResult();
            recording.setMbid(atts.getValue("id"));
        } else if (localName.equals("title") && !inReleaseList) {
            buildString();
        } else if (localName.equals("artist")) {
            inArtist = true;
            recordingArtist = new ReleaseArtist();
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
            results.add(recording);
        } else if (localName.equals("title") && !inReleaseList) {
            recording.setTitle(getString());
        } else if (localName.equals("artist")) {
            inArtist = false;
            recording.setArtist(recordingArtist);
        } else if (localName.equals("name") && inArtist) {
            recordingArtist.setName(getString());
        } else if (localName.equals("length")) {
            recording.setLength(Integer.parseInt(getString()));
        } else if (localName.equals("release-list")) {
            inReleaseList = false;
        }
    }

}
