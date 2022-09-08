package org.metabrainz.android.presentation.features.brainzplayer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.brainzplayer.Playlist.Companion.currentlyPlaying
import org.metabrainz.android.data.sources.brainzplayer.Playlist.Companion.favourite
import org.metabrainz.android.presentation.components.SongViewPager
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.MarqueeText
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.PlayPauseIcon
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.SeekBar
import org.metabrainz.android.presentation.features.brainzplayer.ui.playlist.PlaylistViewModel
import org.metabrainz.android.util.BrainzPlayerExtensions.toSong
import kotlin.math.absoluteValue

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun BrainzPlayerBackDropScreen(
    backdropScaffoldState: BackdropScaffoldState,
    paddingValues: PaddingValues,
    brainzPlayerViewModel: BrainzPlayerViewModel,
    backLayerContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val playlistViewModel = hiltViewModel<PlaylistViewModel>()
    val isShuffled = brainzPlayerViewModel.isShuffled.collectAsState()
    val currentlyPlayingSong = brainzPlayerViewModel.currentlyPlayingSong.collectAsState().value.toSong
    var listenLiked by rememberSaveable{
        mutableStateOf(favourite.items.contains(currentlyPlayingSong))
    }

    BackdropScaffold(
        frontLayerShape = RectangleShape,
        backLayerBackgroundColor = colorResource(id = R.color.app_bg),
        frontLayerScrimColor = Color.Unspecified,
        headerHeight = 136.dp,
        peekHeight = 0.dp,
        scaffoldState = backdropScaffoldState,
        backLayerContent = {
            backLayerContent()
        },
        frontLayerBackgroundColor = colorResource(id = R.color.app_bg),
        frontLayerElevation = 10.dp,
        appBar = {},
        persistentAppBar = false,
        frontLayerContent = {
            AnimatedVisibility (backdropScaffoldState.isConcealed,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    backgroundColor = colorResource(id = R.color.app_bg)
                ) {
                    LazyColumn {
                        item {
                            AlbumArtViewPager(viewModel = brainzPlayerViewModel)
                        }

                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                                    Spacer(modifier = Modifier.height(25.dp))
                                    MarqueeText(
                                        text = currentlyPlayingSong.title,
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(start = 25.dp),
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Start,
                                        color = colorResource(id = R.color.white)
                                    )
                                    MarqueeText(
                                        text = currentlyPlayingSong.artist,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(start = 25.dp),
                                        textAlign = TextAlign.Start,
                                        color = colorResource(id = R.color.white)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                                Icon(
                                    painterResource(id = if (listenLiked) R.drawable.ic_not_liked else R.drawable.ic_liked),
                                    contentDescription = null,
                                    Modifier.clickable {
                                        listenLiked = !listenLiked
                                        scope.launch {
                                            if (listenLiked) playlistViewModel.addSongToPlaylist(currentlyPlayingSong, favourite)
                                            else playlistViewModel.deleteSongFromPlaylist(currentlyPlayingSong, favourite)
                                        }
                                    },
                                    tint = if (!listenLiked) Color.Red else Color.Black
                                )
                            }
                        }
                        item {
                            Box {
                                val progress by brainzPlayerViewModel.progress.collectAsState()
                                SeekBar(
                                    modifier = Modifier
                                        .height(10.dp)
                                        .fillMaxWidth(0.98F)
                                        .padding(10.dp),
                                    progress = progress,
                                    onValueChange = brainzPlayerViewModel::onSeek,
                                    onValueChanged = brainzPlayerViewModel::onSeeked
                                )
                            }
                        }
                        item {
                            val playIcon by brainzPlayerViewModel.playButton.collectAsState()

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 60.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Loop,
                                    contentDescription = "",
                                    modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize),
                                    tint = colorResource(id = R.color.bp_lavender)
                                )

                                Icon(
                                    imageVector = Icons.Rounded.SkipPrevious,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(FloatingActionButtonDefaults.LargeIconSize)
                                        .clickable { brainzPlayerViewModel.skipToPreviousSong() },
                                    tint = colorResource(id = R.color.bp_lavender)
                                )

                                LargeFloatingActionButton(onClick = {
                                    brainzPlayerViewModel.playOrToggleSong(
                                        brainzPlayerViewModel.currentlyPlayingSong.value.toSong,
                                        brainzPlayerViewModel.isPlaying.value,
                                    )
                                }
                                ) {
                                    PlayPauseIcon(
                                        icon = playIcon,
                                        viewModel = brainzPlayerViewModel,
                                        modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Rounded.SkipNext,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(FloatingActionButtonDefaults.LargeIconSize)
                                        .clickable { brainzPlayerViewModel.skipToNextSong() },
                                    tint = colorResource(id = R.color.bp_lavender)
                                )
                                Icon(
                                    imageVector = if (isShuffled.value) Icons.Rounded.ShuffleOn else Icons.Rounded.Shuffle,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(FloatingActionButtonDefaults.LargeIconSize)
                                        .clickable { brainzPlayerViewModel.shuffle() },
                                    tint = colorResource(id = R.color.bp_lavender)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Now Playing",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(start = 25.dp),
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(
                                    id = R.color.white
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        items(items = currentlyPlaying.items) {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(0.98f),
                                backgroundColor = MaterialTheme.colors.onSurface
                            ) {
                                Spacer(modifier = Modifier.height(50.dp))
                                Row(horizontalArrangement = Arrangement.Start) {
                                    AsyncImage(
                                        model = it.albumArt,
                                        contentDescription = "",
                                        error = painterResource(
                                            id = R.drawable.ic_erroralbumart
                                        ),
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier.size(70.dp)
                                    )
                                    Column(Modifier.padding(start = 10.dp)) {
                                        Text(
                                            text = it.title,
                                            color = Color.White
                                        )
                                        Text(
                                            text = it.artist,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(visible = backdropScaffoldState.isRevealed,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Scaffold(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(
                            colorResource(
                                id = R.color.app_bg
                            )
                        )
                ) {
                    SongViewPager()
                }
            }
        })
}

@ExperimentalPagerApi
@Composable
fun AlbumArtViewPager(viewModel: BrainzPlayerViewModel) {
    val songList = viewModel.mediaItem.collectAsState().value
    val currentlyPlayingSong = viewModel.currentlyPlayingSong.collectAsState().value.toSong
    val pagerState = viewModel.pagerState.collectAsState().value
    val pageState = rememberPagerState(initialPage = pagerState)
    songList.data?.let {
        HorizontalPager(count = it.size, state = pageState, modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.app_bg)
            ),
            ) { page ->
            Column(
                Modifier
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .height(280.dp)
                        .padding(top = 20.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(colorResource(id = R.color.app_bg))
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            // We animate the scaleX + scaleY, between 85% and 100%
                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .background(colorResource(id = R.color.app_bg))
                            .fillMaxSize()
                            .padding()
                            .clip(shape = RoundedCornerShape(20.dp))
                            .graphicsLayer { clip = true },
                        model = currentlyPlayingSong.albumArt,
                        contentDescription = "",
                        error = painterResource(
                            id = R.drawable.ic_erroralbumart
                        ),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
            //TODO("Work on ViewPager changing pages")
    }
}