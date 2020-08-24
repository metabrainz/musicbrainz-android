package org.metabrainz.mobile.presentation.features.settings;

import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider;

import static org.metabrainz.mobile.presentation.UserPreferences.PREFERENCE_CLEAR_SUGGESTIONS;
import static org.metabrainz.mobile.presentation.UserPreferences.PREFERENCE_LISTENING_ENABLED;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    private Preference.OnPreferenceChangeListener preferenceChangeListener;

    public void setPreferenceChangeListener(Preference.OnPreferenceChangeListener preferenceChangeListener) {
        this.preferenceChangeListener = preferenceChangeListener;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference(PREFERENCE_CLEAR_SUGGESTIONS).setOnPreferenceClickListener(this);

        if (!App.getInstance().isNotificationServiceAllowed()) {
            ((SwitchPreference) findPreference(PREFERENCE_LISTENING_ENABLED)).setChecked(false);
            UserPreferences.setPreferenceListeningEnabled(false);
        }
        findPreference(PREFERENCE_LISTENING_ENABLED)
                .setOnPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!App.getInstance().isNotificationServiceAllowed()) {
            ((SwitchPreference) findPreference(PREFERENCE_LISTENING_ENABLED)).setChecked(false);
            UserPreferences.setPreferenceListeningEnabled(false);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(PREFERENCE_CLEAR_SUGGESTIONS)) {
            clearSuggestionHistory();
        }
        return true;
    }

    private void clearSuggestionHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(), SuggestionProvider.AUTHORITY,
                SuggestionProvider.MODE);
        suggestions.clearHistory();
        Toast.makeText(getActivity(), R.string.toast_search_cleared, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            startActivity(IntentFactory.getDashboard(getActivity()));
            return true;
        }
        return false;
    }

}
