package org.metabrainz.android.presentation.features.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.preference.PreferenceManager
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.TopAppBar
import org.metabrainz.android.presentation.features.brainzplayer.ui.BrainzPlayerBackDropScreen
import org.metabrainz.android.presentation.features.brainzplayer.ui.BrainzPlayerViewModel
import org.metabrainz.android.presentation.features.onboarding.FeaturesActivity


@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("onboarding", false)) {
            startActivity(Intent(this, FeaturesActivity::class.java))
            finish()
        }

        setContent {
            val brainzPlayerViewModel = hiltViewModel<BrainzPlayerViewModel>()
            val backdropScaffoldState =
                rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
            Scaffold(
                topBar = { TopAppBar(activity = this, title = "Home") },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) { paddingValues ->
                BrainzPlayerBackDropScreen(
                    backdropScaffoldState = backdropScaffoldState,
                    paddingValues = paddingValues,
                    brainzPlayerViewModel = brainzPlayerViewModel
                ) {
                    BackLayerContent(activity = this, applicationContext = LocalContext.current)
                }
            }
        }
    }
}
