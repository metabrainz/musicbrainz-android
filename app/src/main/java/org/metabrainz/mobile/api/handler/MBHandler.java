package org.metabrainz.mobile.api.handler;

import org.xml.sax.helpers.DefaultHandler;

public class MBHandler extends DefaultHandler {

    private StringBuilder sb;

    public void characters(char ch[], int start, int length) {

        if (sb != null) {
            for (int i = start; i < start + length; i++) {
                sb.append(ch[i]);
            }
        }
    }
    
    protected void buildString() {
        sb = new StringBuilder();
    }
    
    protected String getString() {
        return sb.toString();
    }

}
