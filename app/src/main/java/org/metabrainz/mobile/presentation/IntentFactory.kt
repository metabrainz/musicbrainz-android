package org.metabrainz.mobile.presentation

import android.content.Context
import android.content.Intent
import org.metabrainz.mobile.presentation.features.about.AboutActivity
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity
import org.metabrainz.mobile.presentation.features.dashboard.DonateActivity
import org.metabrainz.mobile.presentation.features.login.LoginActivity
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity

object IntentFactory {

    fun getDashboard(context: Context?): Intent {
        val intent = Intent(context, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent
    }

    fun getLogin(context: Context?): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    fun getDonate(context: Context?): Intent {
        return Intent(context, DonateActivity::class.java)
    }

    fun getAbout(context: Context?): Intent {
        return Intent(context, AboutActivity::class.java)
    }

    fun getSettings(context: Context?): Intent {
        return Intent(context, SettingsActivity::class.java)
    }
}