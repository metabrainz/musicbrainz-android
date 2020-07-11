package org.metabrainz.mobile.presentation;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.util.Utils;

public abstract class MusicBrainzActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        return true;
    }

    protected abstract Uri getBrowserURI();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            startActivity(IntentFactory.getDashboard(getApplicationContext()));
            return true;
        } else if (itemId == R.id.menu_preferences) {
            startActivity(IntentFactory.getSettings(getApplicationContext()));
            return true;
        } else if (itemId == R.id.menu_feedback) {
            sendFeedback();
            return true;
        } else if (itemId == R.id.menu_login) {
            startActivity(IntentFactory.getLogin(getApplicationContext()));
            return true;
        } else if (itemId == R.id.menu_open_website) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(getBrowserURI());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (UserPreferences.getSystemLanguagePreference()) {
            Context context = Utils.changeLanguage(newBase, "en");
            super.attachBaseContext(context);
        } else super.attachBaseContext(newBase);
    }

    private void sendFeedback() {
        try {
            startActivity(Utils.emailIntent(Configuration.FEEDBACK_EMAIL, Configuration.FEEDBACK_SUBJECT));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.toast_feedback_fail, Toast.LENGTH_LONG).show();
        }
    }

}
