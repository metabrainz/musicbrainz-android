package org.metabrainz.mobile.activity;

import android.os.Bundle;
import android.view.Window;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.fragment.BarcodeSearchFragment.LoadingCallbacks;

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
