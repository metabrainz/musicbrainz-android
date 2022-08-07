package org.metabrainz.android.presentation.features.brainzplayer.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import org.metabrainz.android.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.presentation.components.BrainzPlayerBottomBar

@AndroidEntryPoint
class BrainzPlayerActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val brainzPlayerViewModel = hiltViewModel<BrainzPlayerViewModel>()
            val backdropScaffoldState =
                rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    org.metabrainz.android.presentation.components.TopAppBar(
                        activity = this,
                        title = "BrainzPlayer"
                    )
                },
                bottomBar = { BrainzPlayerBottomBar(activity = this) },
                backgroundColor = colorResource(id = R.color.app_bg)
            ) { paddingValues ->
                BrainzPlayerBackDropScreen(
                    backdropScaffoldState = backdropScaffoldState ,
                    activity = this@BrainzPlayerActivity ,
                    paddingValues = paddingValues ,
                    brainzPlayerViewModel =  brainzPlayerViewModel
                ) {
                    Column(modifier = Modifier
                        .fillMaxSize()) {
                        BrainzPlayerActivityBackScreenContent()
                    }
                }
            }
        }
    }
}

@Composable
fun BrainzPlayerActivityBackScreenContent() {
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
                Text(text = "Artists",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.white))
                LazyRow{
                    items(5){
                        BrainzPlayerActivityCards(Icons.Rounded.Person)
                    }
                }
            }
        }
        item {
            Column {
                Text(text = "Albums",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = colorResource(id = R.color.white))
                LazyRow{
                    items(5){
                        BrainzPlayerActivityCards(icon = Icons.Rounded.Album)
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
                    items(5) {
                        BrainzPlayerActivityCards(icon = Icons.Rounded.QueueMusic)
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
    val gradientColors = Brush.horizontalGradient(0f to Color(0xff353070), 1000f to Color(0xffFFA500) )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(gradientColors)
            .height(120.dp),
    ) {

        Column {
            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "",
                Modifier
                    .size(30.dp)
                    .padding(start = 3.dp, top = 3.dp), tint = Color.White)
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
    val gradientColors = Brush.verticalGradient(0f to Color(0xff263238), 100f to Color(0xff324147) )
    Box(modifier = Modifier
        .height(175.dp)
        .width(180.dp)
        .padding(10.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(gradientColors)
        .border(color = Color(0xff324147), width = 1.dp, shape = RoundedCornerShape(8.dp))) {

    }
}

@Composable
fun BrainzPlayerActivityCards( icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(180.dp)
            .padding(10.dp)
            .clip(CircleShape)
            .background(color = colorResource(id = R.color.bp_bottom_song_viewpager))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize(0.8f)
                .align(Alignment.Center),
            tint = colorResource(id = R.color.light_gray)
        )
    }
}
