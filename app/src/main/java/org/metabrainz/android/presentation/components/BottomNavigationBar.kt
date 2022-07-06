package org.metabrainz.android.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.coroutines.flow.distinctUntilChanged
import org.metabrainz.android.R
import org.metabrainz.android.presentation.features.brainzplayer.ui.BrainzPlayerViewModel
import org.metabrainz.android.presentation.features.dashboard.DashboardActivity
import org.metabrainz.android.presentation.features.dashboard.DashboardActivity.Companion.currentlyPayingSong
import org.metabrainz.android.presentation.features.listens.ListensActivity
import org.metabrainz.android.presentation.features.login.LoginActivity
import org.metabrainz.android.presentation.features.navigation.NavigationItem
import org.metabrainz.android.presentation.features.newsbrainz.NewsBrainzActivity
import org.metabrainz.android.util.BrainzPlayerExtensions.toSong

@Composable
fun BottomNavigationBar(activity: Activity) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.News,
        NavigationItem.Listens,
        NavigationItem.Critiques,
        NavigationItem.Profile,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.app_bg),
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title, tint = Color.Unspecified) },
                label = { Text(text = item.title) },
                selectedContentColor = colorResource(id = R.color.white),
                unselectedContentColor = colorResource(id = R.color.gray),
                alwaysShowLabel = true,
                selected = true,
                onClick = {
                    when(item.route){
                        "home" -> {
                            val nextActivity = DashboardActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, DashboardActivity::class.java))
                            }
                        }
                        "news" -> {
                            val nextActivity = NewsBrainzActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, NewsBrainzActivity::class.java))
                            }
                        }
                        "listens" -> {
                            val nextActivity = ListensActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, ListensActivity::class.java))
                            }
                        }
                        "critiques" -> {
                            FinestWebView.Builder(activity).show("https://critiquebrainz.org/")
                        }
                        "profile" -> {
                            val nextActivity = LoginActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, LoginActivity::class.java))
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SongViewPager(viewModel: BrainzPlayerViewModel) {
    val songList = viewModel.mediaItems.collectAsState().value
    val currentPlayingSong = viewModel.currentlyPlayingSong.collectAsState().value
    currentlyPayingSong = currentPlayingSong.toSong
    val pageState = rememberPagerState()
    songList.data?.let {

        HorizontalPager(count = it.size, state = pageState, modifier = Modifier.background(
            colorResource(id = R.color.app_bg))) { page ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(15.dp),
                backgroundColor = colorResource(id = R.color.light_blue)
            ) {
                Row {
                    Column(Modifier.padding(start = 50.dp, top = 8.dp)) {
                        Text(text = it[page].title, fontWeight = FontWeight.Bold)
                        Text(text = it[page].artist)
                    }
                }
            }
        }
        LaunchedEffect(pageState) {
            snapshotFlow { pageState.currentPage }
                .distinctUntilChanged()
                .collect { page->

                    if (page>songList.data.indexOf(currentlyPayingSong)) {
                        viewModel.skipToNextSong()
                        viewModel.playOrToggleSong(songList.data[page])
                    }
                    else if (page<songList.data.indexOf(currentlyPayingSong)) {
                        viewModel.skipToPreviousSong()
                        viewModel.playOrToggleSong(songList.data[page])
                    }
                }
        }
    }
}