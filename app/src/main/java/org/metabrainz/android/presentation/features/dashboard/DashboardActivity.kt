package org.metabrainz.android.presentation.features.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.brainzplayer.Song
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.SongViewPager
import org.metabrainz.android.presentation.components.TopAppBar
import org.metabrainz.android.presentation.features.brainzplayer.ui.BrainzPlayerViewModel
import org.metabrainz.android.presentation.features.onboarding.FeaturesActivity


@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    companion object {
        var currentlyPayingSong: Song? = null
    }

    @OptIn(ExperimentalMaterialApi::class)
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
                rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
            LaunchedEffect(backdropScaffoldState) {
                backdropScaffoldState.reveal()
            }
            Scaffold(
                topBar = { TopAppBar(activity = this, title = "Home") },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) { paddingValues ->
                BackdropScaffold(
                    frontLayerShape = RectangleShape,
                    backLayerBackgroundColor = colorResource(id = R.color.app_bg),
                    frontLayerScrimColor = Color.Unspecified,
                    headerHeight = 136.dp,
                    peekHeight = 0.dp,
                    scaffoldState = backdropScaffoldState,
                    backLayerContent = {
                        BackLayerContent(activity = this, applicationContext = applicationContext)
                    },
                    appBar = {},
                    persistentAppBar = false,
                    frontLayerContent = {
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                                .background(
                                    colorResource(
                                        id = R.color.app_bg
                                    )
                                )
                        ) {
                            SongViewPager(viewModel = brainzPlayerViewModel)
                        }
                    }) {
                }
            }
        }
    }
}
