package org.musicbrainz.mobile.intent;

import org.musicbrainz.mobile.activity.WebActivity;

import android.content.Context;
import android.content.Intent;

public class IntentFactory {
    
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TARGET_URL = "target_url";

    public static Intent getWebViewIntent(Context context, int titleId, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_TITLE, titleId);
        intent.putExtra(EXTRA_TARGET_URL, url);
        return intent;
    }

}
