package org.metabrainz.android.ui.screens.settings

import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.MenuItem
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.metabrainz.android.R
import org.metabrainz.android.ui.screens.dashboard.DashboardActivity
import org.metabrainz.android.ui.screens.suggestion.SuggestionProvider
import org.metabrainz.android.util.UserPreferences.PREFERENCE_CLEAR_SUGGESTIONS
import org.metabrainz.android.util.UserPreferences.PREFERENCE_SYSTEM_THEME

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    private var preferenceChangeListener: Preference.OnPreferenceChangeListener? = null

    fun setPreferenceChangeListener(preferenceChangeListener: Preference.OnPreferenceChangeListener?) {
        this.preferenceChangeListener = preferenceChangeListener
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>(PREFERENCE_CLEAR_SUGGESTIONS)!!.onPreferenceClickListener = this
        findPreference<Preference>(PREFERENCE_SYSTEM_THEME)!!.onPreferenceChangeListener = preferenceChangeListener
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (preference.key == PREFERENCE_CLEAR_SUGGESTIONS) {
            clearSuggestionHistory()
        }
        return true
    }

    private fun clearSuggestionHistory() {
        val suggestions = SearchRecentSuggestions(activity, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE)
        suggestions.clearHistory()
        Toast.makeText(activity, R.string.toast_search_cleared, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(context, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}