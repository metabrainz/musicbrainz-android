package org.musicbrainz.mobile.async.external.result;

import java.util.List;

public class WikipediaResponse {

    public MobileView mobileview;
    
    public class MobileView {
        public List<Section> sections;
    }
    
    public class Section {
        public String text;
    }
    
}
