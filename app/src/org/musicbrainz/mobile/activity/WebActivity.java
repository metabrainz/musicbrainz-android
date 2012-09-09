package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.WebFragment.WebFragmentCallbacks;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.os.Bundle;

import com.actionbarsherlock.view.Window;

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
