package org.metabrainz.mobile.presentation.features.settings

import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.MenuItem
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.metabrainz.mobile.App
import org.metabrainz.mobile.R
import org.metabrainz.mobile.presentation.IntentFactory.getDashboard
import org.metabrainz.mobile.presentation.UserPreferences.PREFERENCE_CLEAR_SUGGESTIONS
import org.metabrainz.mobile.presentation.UserPreferences.PREFERENCE_LISTENING_ENABLED
import org.metabrainz.mobile.presentation.UserPreferences.preferenceListeningEnabled
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    private var preferenceChangeListener: Preference.OnPreferenceChangeListener? = null
    fun setPreferenceChangeListener(preferenceChangeListener: Preference.OnPreferenceChangeListener?) {
        this.preferenceChangeListener = preferenceChangeListener
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>(PREFERENCE_CLEAR_SUGGESTIONS)!!.onPreferenceClickListener = this
        if (!App.context!!.isNotificationServiceAllowed) {
            (findPreference<Preference>(PREFERENCE_LISTENING_ENABLED) as SwitchPreference?)!!.isChecked = false
            preferenceListeningEnabled = false
        }
        findPreference<Preference>(PREFERENCE_LISTENING_ENABLED)!!.onPreferenceChangeListener = preferenceChangeListener
    }

    override fun onResume() {
        super.onResume()
        if (!App.context!!.isNotificationServiceAllowed) {
            (findPreference<Preference>(PREFERENCE_LISTENING_ENABLED) as SwitchPreference?)!!.isChecked = false
            preferenceListeningEnabled = false
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (preference.key == PREFERENCE_CLEAR_SUGGESTIONS) {
            clearSuggestionHistory()
        }
        return true
    }

    private fun clearSuggestionHistory() {
        val suggestions = SearchRecentSuggestions(activity, SuggestionProvider.AUTHORITY,
                SuggestionProvider.MODE)
        suggestions.clearHistory()
        Toast.makeText(activity, R.string.toast_search_cleared, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            startActivity(getDashboard(activity))
            return true
        }
        return false
    }
}