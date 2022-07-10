package org.metabrainz.android.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.metabrainz.android.R
import org.metabrainz.android.presentation.features.brainzplayer.ui.BrainzPlayerViewModel
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.MarqueeText
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.PlayPauseIcon
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.SeekBar
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
    val currentPlayingSong = viewModel.currentlyPlayingSong.collectAsState()
    currentlyPayingSong = currentPlayingSong.value.toSong
    val pageState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    songList.data?.let {

        HorizontalPager(count = it.size, state = pageState, modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.app_bg)
            )) { page ->
            Column {
                Box {
                    val progress by viewModel.progress.collectAsState()
                    SeekBar(
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth(0.98F)
                            .padding(10.dp),
                        progress = progress,
                        onValueChange = viewModel::onSeek,
                        onValueChanged = viewModel::onSeeked
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .height(56.dp),
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = colorResource(id = R.color.light_blue)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .height(45.dp)
                                .width(45.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .graphicsLayer { clip = true },
                                model = it[page].albumArt,
                                contentDescription = "",
                                error = painterResource(
                                    id = R.drawable.ic_musicbrainz_logo_icon
                                ),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            Modifier
                                .width(255.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 20.dp)
                        ) {
                            MarqueeText(text = it[page].title, fontWeight = FontWeight.Bold)
                            MarqueeText(text = it[page].artist)
                        }
                        val playIcon by viewModel.playButton.collectAsState()
                        Column(
                            Modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxWidth()
                                .padding(end = 10.dp), horizontalAlignment = Alignment.End
                        ) {
                            PlayPauseIcon(playIcon, viewModel)

                        }
                    }
                }
            }
        }
        LaunchedEffect(key1= pageState) {
            snapshotFlow { pageState.currentPage }
                .distinctUntilChanged()
                .filter { pageState.currentPage>=0 }
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
        scope.launch {
            if (songList.data.indexOf(currentlyPayingSong) == -1) pageState.animateScrollToPage(0)
            else pageState.scrollToPage(
                songList.data.indexOf(
                    currentlyPayingSong
                )
            )
        }
    }
}