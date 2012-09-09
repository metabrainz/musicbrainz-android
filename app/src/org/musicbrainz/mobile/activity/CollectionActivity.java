package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.CollectionFragment.FragmentLoadingCallbacks;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

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
