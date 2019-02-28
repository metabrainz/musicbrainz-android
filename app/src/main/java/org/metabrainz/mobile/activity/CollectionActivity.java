package org.metabrainz.mobile.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.fragment.CollectionFragment.FragmentLoadingCallbacks;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.intent.IntentFactory.Extra;

public class CollectionActivity extends MusicBrainzActivity implements FragmentLoadingCallbacks {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setSupportProgressBarIndeterminateVisibility(false);
        setTitleFromIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitleFromIntent() {
        String collectionName = getIntent().getStringExtra(Extra.TITLE);
        getSupportActionBar().setTitle(collectionName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(IntentFactory.getCollectionList(getApplicationContext()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadStart() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onLoadFinish() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

}
