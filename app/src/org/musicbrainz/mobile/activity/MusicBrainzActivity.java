package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.util.Utils;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public abstract class MusicBrainzActivity extends SherlockFragmentActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            startActivity(IntentFactory.getDashboard(getApplicationContext()));
            return true;
        case R.id.menu_preferences:
            startActivity(IntentFactory.getSettings(getApplicationContext()));
            return true;
        case R.id.menu_feedback:
            sendFeedback();
            return true;
        case R.id.legacy_search:
            onSearchRequested();
            return true;
        case R.id.menu_login:
            startActivity(IntentFactory.getLogin(getApplicationContext()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendFeedback() {
        try {
            startActivity(Utils.emailIntent(Configuration.FEEDBACK_EMAIL, Configuration.FEEDBACK_SUBJECT));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.toast_feedback_fail, Toast.LENGTH_LONG).show();
        }
    }
    
}
