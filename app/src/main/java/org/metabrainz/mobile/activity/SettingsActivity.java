package org.metabrainz.mobile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.view.MenuItem;
import android.widget.Toast;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.suggestion.SuggestionProvider;
import org.metabrainz.mobile.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener {

    private static final String CLEAR_SUGGESTIONS = "clear_suggestions";
    OnSharedPreferenceChangeListener prefChanges = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            requestBackup();
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        addPreferencesFromResource(R.xml.preferences);
        findPreference(CLEAR_SUGGESTIONS).setOnPreferenceClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .registerOnSharedPreferenceChangeListener(prefChanges);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(prefChanges);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(CLEAR_SUGGESTIONS)) {
            clearSuggestionHistory();
            return true;
        }
        return false;
    }

    private void clearSuggestionHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY,
                SuggestionProvider.MODE);
        suggestions.clearHistory();
        Toast.makeText(this, R.string.toast_search_cleared, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(IntentFactory.getDashboard(getApplicationContext()));
                return true;
        }
        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void requestBackup() {
        try {
            Class managerClass = Class.forName("android.app.backup.BackupManager");
            Constructor managerConstructor = managerClass.getConstructor(Context.class);
            Object manager = managerConstructor.newInstance(getApplicationContext());
            Method m = managerClass.getMethod("dataChanged");
            m.invoke(manager);
        } catch (ClassNotFoundException e) {
            Log.d("Backup manager not found");
        } catch (Throwable t) {
            Log.e(t.getMessage());
        }
    }

}
