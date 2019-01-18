package org.metabrainz.android.activity;

import org.metabrainz.android.R;
import org.metabrainz.android.fragment.WebFragment.WebFragmentCallbacks;
import org.metabrainz.android.intent.IntentFactory.Extra;

import android.os.Bundle;

import android.view.Window;

public class WebActivity extends MusicBrainzActivity implements WebFragmentCallbacks {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getSupportActionBar().setTitle(getIntent().getIntExtra(Extra.TITLE, R.string.app_name));
    }

    @Override
    public void onPageStart() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onPageFinish() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

}
