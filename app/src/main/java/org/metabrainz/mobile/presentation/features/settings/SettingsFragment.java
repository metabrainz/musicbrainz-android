package org.metabrainz.mobile.presentation.features.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.SearchRecentSuggestions;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceFragmentCompat;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider;
import org.metabrainz.mobile.util.Utils;

import static android.app.Activity.RESULT_OK;
import static org.metabrainz.mobile.App.DIRECTORY_SELECT_REQUEST_CODE;
import static org.metabrainz.mobile.App.STORAGE_PERMISSION_REQUEST_CODE;

public class SettingsFragment extends PreferenceFragmentCompat implements androidx.preference.Preference.OnPreferenceClickListener {

    private static final String CLEAR_SUGGESTIONS = "clear_suggestions";
    private static final String TAGGER_DIRECTORY = "tagger_directory";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference(CLEAR_SUGGESTIONS).setOnPreferenceClickListener(this);
        findPreference(TAGGER_DIRECTORY).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(androidx.preference.Preference preference) {
        if (preference.getKey().equals(CLEAR_SUGGESTIONS)) {
            clearSuggestionHistory();
            return true;
        } else if (preference.getKey().equals(TAGGER_DIRECTORY)) {
            String[] permissions = Utils.getPermissionsList(getContext());
            if (permissions.length > 0) ActivityCompat.requestPermissions(getActivity(),
                    permissions, STORAGE_PERMISSION_REQUEST_CODE);
            else chooseDirectory();
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

    private void chooseDirectory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String path = Environment.getExternalStorageDirectory().getPath().concat("/Picard/");
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(path));
            startActivityForResult(intent, DIRECTORY_SELECT_REQUEST_CODE);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIRECTORY_SELECT_REQUEST_CODE && resultCode == RESULT_OK) {
            //TODO: Implement option to let users select the tagger directory
        }
    }
}
