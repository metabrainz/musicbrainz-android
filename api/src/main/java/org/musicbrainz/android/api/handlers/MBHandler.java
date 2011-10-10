package org.musicbrainz.android.api.handlers;

import org.xml.sax.helpers.DefaultHandler;

public class MBHandler extends DefaultHandler {

    protected StringBuilder sb;

    public void characters(char ch[], int start, int length) {

        if (sb != null) {
            for (int i = start; i < start + length; i++) {
                sb.append(ch[i]);
            }
        }
    }

}
