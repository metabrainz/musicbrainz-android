package org.metabrainz.android.presentation

import android.content.Context
import android.content.Intent
import org.metabrainz.android.presentation.features.settings.SettingsActivity

object IntentFactory {
    fun getSettings(context: Context?): Intent {
        return Intent(context, SettingsActivity::class.java)
    }
}