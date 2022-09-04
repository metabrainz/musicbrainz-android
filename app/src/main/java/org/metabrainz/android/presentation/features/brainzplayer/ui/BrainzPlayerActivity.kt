package org.metabrainz.android.presentation.features.brainzplayer.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import org.metabrainz.android.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.data.sources.brainzplayer.Album
import org.metabrainz.android.data.sources.brainzplayer.Artist
import org.metabrainz.android.data.sources.brainzplayer.Playlist
import org.metabrainz.android.data.sources.brainzplayer.Playlist.Companion.recentlyPlayed
import org.metabrainz.android.presentation.components.BrainzPlayerBottomBar
import org.metabrainz.android.presentation.components.TopAppBar
import org.metabrainz.android.presentation.features.brainzplayer.ui.album.AlbumViewModel
import org.metabrainz.android.presentation.features.brainzplayer.ui.artist.ArtistViewModel
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.Navigation
import org.metabrainz.android.presentation.features.brainzplayer.ui.components.forwardingPainter
import org.metabrainz.android.presentation.features.brainzplayer.ui.playlist.PlaylistViewModel

@ExperimentalPagerApi
@AndroidEntryPoint
class BrainzPlayerActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val brainzPlayerViewModel = hiltViewModel<BrainzPlayerViewModel>()
            val albumViewModel = hiltViewModel<AlbumViewModel>()
            val artistViewModel = hiltViewModel<ArtistViewModel>()
            val playlistViewModel = hiltViewModel<PlaylistViewModel>()
            val artists = artistViewModel.artists.collectAsState(initial = listOf()).value
            val albums = albumViewModel.albums.collectAsState(initial = listOf()).value
            val playlists by playlistViewModel.playlists.collectAsState(initial = listOf())

            val backdropScaffoldState =
                rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        activity = this,
                        title = "BrainzPlayer"
                    )
                },
                bottomBar = { BrainzPlayerBottomBar(navController) },
                backgroundColor = colorResource(id = R.color.app_bg)
            ) { paddingValues ->
                BrainzPlayerBackDropScreen(
                    backdropScaffoldState = backdropScaffoldState,
                    paddingValues = paddingValues,
                    brainzPlayerViewModel = brainzPlayerViewModel
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Navigation(navController, albums, artists, playlists, recentlyPlayed)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    albums: List<Album>,
    artists: List<Artist>,
    playlists: List<Playlist>,
    recentlyPlayedSongs: Playlist,
    navHostController: NavHostController
) {
    val searchTextState = remember {
        mutableStateOf(TextFieldValue(""))
    }
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchView(state = searchTextState)
            }
        }
        item {
            ListenBrainzHistoryCard()
        }
        item {
            Column {
                Text(
                    text = "Recently Played",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.white)
                )
                LazyRow {
                    items(5) {
                        RecentlyPlayedCard()
                    }
                }
            }
        }
        item {
            Column {
                Text(
                    text = "Artists",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.white)
                )
                LazyRow {
                    items(items = artists) {
                        BrainzPlayerActivityCards(icon = "",
                            errorIcon = R.drawable.ic_artist,
                            title = it.name,
                            modifier = Modifier
                                .clickable {
                                    navHostController.navigate("onArtistClick/${it.id}")
                                }
                        )
                    }
                }
            }
        }
        item {
            Column {
                Text(
                    text = "Albums",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.white)
                )
                LazyRow {
                    items(albums) {
                        BrainzPlayerActivityCards(it.albumArt,
                            R.drawable.ic_album,
                            title = it.title,
                            modifier = Modifier
                                .clickable {
                                    navHostController.navigate("onAlbumClick/${it.albumId}")
                                }
                        )
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Playlists",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.white)
                )
                LazyRow {
                    items(playlists.filter {
                        it.id != (-1).toLong()
                    }) {
                        BrainzPlayerActivityCards(
                            icon = "",
                            errorIcon = it.art,
                            title = it.title,
                            modifier = Modifier.clickable { navHostController.navigate("onPlaylistClick/${it.id}") })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(modifier= Modifier
        .fillMaxWidth(0.9f)
        .padding(2.dp),
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        textStyle = TextStyle(Color.White, fontSize = 15.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(onClick = {
                    state.value = TextFieldValue("")
                }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.textFieldColors(

            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ListenBrainzHistoryCard() {
    val gradientColors =
        Brush.horizontalGradient(0f to Color(0xff353070), 1000f to Color(0xffFFA500))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(gradientColors)
            .height(120.dp),
    ) {
        Column {
            Icon(
                imageVector = Icons.Rounded.PlayArrow, contentDescription = "",
                Modifier
                    .size(30.dp)
                    .padding(start = 3.dp, top = 3.dp), tint = Color.White
            )
            Text(
                text = "Listen to \nplayback history",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                textAlign = TextAlign.Start,
                color = Color.White
            )
        }
    }
}

@Composable
fun RecentlyPlayedCard() {
    val gradientColors = Brush.verticalGradient(0f to Color(0xff263238), 100f to Color(0xff324147))
    Box(
        modifier = Modifier
            .height(175.dp)
            .width(180.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(gradientColors)
            .border(color = Color(0xff324147), width = 1.dp, shape = RoundedCornerShape(8.dp))
    )
}

@Composable
fun BrainzPlayerActivityCards(icon: String, errorIcon : Int, title: String, modifier : Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .height(200.dp)
            .width(180.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = modifier
                    .padding(10.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(id = R.color.bp_bottom_song_viewpager))
                    .size(150.dp)
            ) {
                AsyncImage(
                    modifier = modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .clip(CircleShape),
                    model = icon,
                    contentDescription = "",
                    error = forwardingPainter(
                        painter = painterResource(id = errorIcon)
                    ) { info ->
                        inset(25f, 25f) {
                            with(info.painter) {
                                draw(size, info.alpha, info.colorFilter)
                            }
                        }
                    },
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(id = R.color.white)
            )
        }
    }
}