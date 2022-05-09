package org.metabrainz.android.presentation.features.listens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.ListenCard
import org.metabrainz.android.presentation.components.TopAppBar
import org.metabrainz.android.presentation.features.login.LoginActivity
import org.metabrainz.android.presentation.features.login.LoginSharedPreferences.username

@AndroidEntryPoint
class ListensActivity: ComponentActivity() {
    var loading =  mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                backgroundColor = colorResource(id = R.color.app_bg),
                topBar = { TopAppBar(activity = this, title = "Listens") },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) {
                AllUserListens(
                    modifier = Modifier.padding(it),
                    activity = this
                )
            }
        }
    }
}

@Composable
fun AllUserListens(
    modifier: Modifier = Modifier,
    viewModel: ListensViewModel = viewModel(),
    activity: Activity
) {
    if(username == ""){
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                })
                { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    activity.finish()
                })
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Please login to your profile") },
            text = { Text(text = "We will fetch your listens once you have logged in") }
        )
        return
    }

    username?.let { viewModel.fetchUserListens(userName = it) }

    AnimatedVisibility(
        visible = viewModel.isLoading,
        enter = fadeIn(initialAlpha = 0.4f),
        exit = fadeOut(animationSpec = tween(durationMillis = 250))
    ){
        Loader()
    }
    LazyColumn(modifier) {
            items(viewModel.listens) { listen->
                ListenCard(
                    listen,
                    coverArt = listen.coverArt,
                    onItemClicked = { listen ->

                    }
                )
            }
    }
}

@Composable
fun Loader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.headphone_meb_loading))
    LottieAnimation(composition)
}