package org.metabrainz.android.presentation.features.listens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.ListenCard
import org.metabrainz.android.presentation.components.TopAppBar

@AndroidEntryPoint
class ListensActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                backgroundColor = colorResource(id = R.color.app_bg),
                topBar = { TopAppBar(activity = this, title = "Listens") },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) {
                AllUserListens()
            }
        }
    }
}

@Composable
fun AllUserListens(
    viewModel: ListensViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    viewModel.fetchUserListens("akshaaatt")
    LazyColumn(modifier) {
            items(viewModel.listens) {listen->
                when {
                    listen.track_metadata.additional_info.release_mbid!=null -> {
                        viewModel.fetchCoverArt(listen.track_metadata.additional_info.release_mbid)
                    }
                }
                ListenCard(
                    listen,
                    viewModel.coverArt,
                    onItemClicked = { listen ->

                    }
                )
            }
    }
}