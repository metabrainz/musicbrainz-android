package org.metabrainz.mobile.presentation;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import androidx.browser.customtabs.CustomTabsIntent;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.activity.MusicBrainzActivity;

public class DonateActivity extends MusicBrainzActivity {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(getResources().getColor(R.color.colorPrimaryDark))
                .setShowTitle(true)
                .build();
        customTabsIntent.launchUrl(this,
                Uri.parse("https://metabrainz.org/donate"));
    }

    @Override
    protected void onResume() {
        finish();
        super.onResume();
    }
}
