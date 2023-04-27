package org.metabrainz.android.ui.screens.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.ui.components.BottomNavigationBar
import org.metabrainz.android.ui.components.TopAppBar
import org.metabrainz.android.ui.theme.MusicBrainzTheme
import org.metabrainz.android.ui.screens.onboarding.FeaturesActivity

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setUiMode()
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("onboarding", false)) {
            startActivity(Intent(this, FeaturesActivity::class.java))
            finish()
        }
        setContent {
            MusicBrainzTheme {
                Scaffold(
                    topBar = { TopAppBar(activity = this, title = "Home") },
                    bottomBar = { BottomNavigationBar(activity = this) },
                    backgroundColor = MaterialTheme.colorScheme.background
                ) {
                    BackLayerContent(
                        activity = this,
                        applicationContext = LocalContext.current,
                        padding = it
                    )
                }
            }
        }
    }
    
    // Sets Ui mode for XML layouts.
    private fun setUiMode(){
        when( PreferenceManager.getDefaultSharedPreferences(this)
            .getString("app_theme", getString(R.string.settings_device_theme_use_device_theme)) )
        {
            getString(R.string.settings_device_theme_dark) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
            getString(R.string.settings_device_theme_light) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
