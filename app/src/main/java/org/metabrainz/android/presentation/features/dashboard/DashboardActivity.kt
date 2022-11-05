package org.metabrainz.android.presentation.features.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.TopAppBar
import org.metabrainz.android.presentation.features.onboarding.FeaturesActivity

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("onboarding", false)) {
            startActivity(Intent(this, FeaturesActivity::class.java))
            finish()
        }
        setContent {
            Scaffold(
                topBar = { TopAppBar(activity = this, title = "Home") },
                bottomBar = { BottomNavigationBar(activity = this) },
            ){
                BackLayerContent(
                    activity = this,
                    applicationContext = LocalContext.current,
                    padding = it
                )
            }
        }
    }
}
