package org.metabrainz.android.activity;

import org.metabrainz.android.R;
import org.metabrainz.android.fragment.BarcodeSearchFragment.LoadingCallbacks;

import android.os.Bundle;

import android.view.Window;

/**
 * Activity to submit a barcode to a selected release in MusicBrainz.
 */
public class BarcodeSearchActivity extends MusicBrainzActivity implements LoadingCallbacks {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void startLoading() {
        setSupportProgressBarIndeterminateVisibility(true);        
    }

    @Override
    public void stopLoading() {
        setSupportProgressBarIndeterminateVisibility(false);        
    }

}
