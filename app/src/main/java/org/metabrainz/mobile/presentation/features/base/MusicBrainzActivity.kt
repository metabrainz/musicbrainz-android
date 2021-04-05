package org.metabrainz.mobile.presentation.features.base;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.databinding.LayoutToolbarBinding;
import org.metabrainz.mobile.presentation.Configuration;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.util.Utils;

import java.util.Objects;

public abstract class MusicBrainzActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        return true;
    }

    protected Uri getBrowserURI() {
        return Uri.EMPTY;
    }

    protected void setupToolbar(ViewBinding binding) {
        LayoutToolbarBinding toolbarBinding = LayoutToolbarBinding.bind(binding.getRoot());
        setSupportActionBar(toolbarBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
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
            return true;
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
