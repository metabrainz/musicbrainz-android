package org.musicbrainz.mobile.intent;

import org.musicbrainz.mobile.activity.WebActivity;

import android.content.Context;
import android.content.Intent;

public class IntentFactory {
    
    public interface Extra {
        
        public static final String ARTIST_MBID = "ambid";
        public static final String RELEASE_MBID = "rmbid";
        public static final String RG_MBID = "rgmbid";
        public static final String BARCODE = "barcode";
        
        public static final String TYPE = "type";
        public static final String QUERY = "query";
        public static final String ARTIST = "artist";
        public static final String RELEASE_GROUP = "rg";
        public static final String LABEL = "label";
        public static final String RECORDING = "recording";
        public static final String ALL = "all";
        
        public static final String MESSAGE = "message";
        
        public static final String TITLE = "title";
        public static final String TARGET_URL = "target_url";
    }
    

    public static Intent getWebViewIntent(Context context, int titleId, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Extra.TITLE, titleId);
        intent.putExtra(Extra.TARGET_URL, url);
        return intent;
    }

}
