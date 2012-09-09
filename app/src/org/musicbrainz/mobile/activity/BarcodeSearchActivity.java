package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.BarcodeSearchFragment.LoadingCallbacks;

import android.os.Bundle;

import com.actionbarsherlock.view.Window;

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
