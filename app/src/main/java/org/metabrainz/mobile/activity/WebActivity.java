package org.metabrainz.mobile.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import androidx.browser.customtabs.CustomTabsIntent;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.intent.IntentFactory.Extra;

public class WebActivity extends MusicBrainzActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getIntent().getIntExtra(Extra.TITLE, R.string.app_name));

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                .build();
        customTabsIntent.launchUrl(this,
                Uri.parse("https://metabrainz.org/donate"));
    }
}
