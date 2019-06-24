package org.metabrainz.mobile.presentation.features.settings;

import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider;

class SettingsFragment extends PreferenceFragmentCompat implements androidx.preference.Preference.OnPreferenceClickListener {

    private static final String CLEAR_SUGGESTIONS = "clear_suggestions";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference(CLEAR_SUGGESTIONS).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(androidx.preference.Preference preference) {
        if (preference.getKey().equals(CLEAR_SUGGESTIONS)) {
            clearSuggestionHistory();
            return true;
        }
        return false;
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

        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(IntentFactory.getDashboard(getActivity()));
                return true;
        }
        return false;
    }
}
