package org.metabrainz.android.ui.screens.settings

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityPreferencesBinding
import org.metabrainz.android.ui.theme.isUiModeIsDark
import org.metabrainz.android.util.UserPreferences.PREFERENCE_SYSTEM_THEME

class SettingsActivity : AppCompatActivity() {

    var preferenceChangeListener: Preference.OnPreferenceChangeListener? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            if (fragment is SettingsFragment) {
                fragment.setPreferenceChangeListener(preferenceChangeListener)
            }
        }

        preferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
            // Explicit Ui Mode functionality.
            if (preference.key == PREFERENCE_SYSTEM_THEME){
                when (newValue) {
                    "Dark" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        isUiModeIsDark.value = true
                    }
                    "Light" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        isUiModeIsDark.value = false
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        isUiModeIsDark.value = null
                    }
                }
                return@OnPreferenceChangeListener true
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}