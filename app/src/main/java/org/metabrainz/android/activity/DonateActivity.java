package org.metabrainz.android.activity;

import org.metabrainz.android.R;
import org.metabrainz.android.fragment.DonateFragment.DonationCallbacks;

import android.os.Bundle;

import android.view.Window;

public class DonateActivity extends MusicBrainzActivity implements DonationCallbacks {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public void startLoading() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void stopLoading() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onResult() {
        finish();
    }

}
