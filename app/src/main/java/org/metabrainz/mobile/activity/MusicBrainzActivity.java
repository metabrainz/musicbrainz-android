package org.metabrainz.mobile.activity;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.config.Configuration;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.util.Utils;

public abstract class MusicBrainzActivity extends AppCompatActivity {

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
