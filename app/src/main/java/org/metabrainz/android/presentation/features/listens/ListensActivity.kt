package org.metabrainz.android.presentation.features.listens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.ListensCard
import org.metabrainz.android.presentation.components.TopAppBar

@AndroidEntryPoint
class ListensActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                backgroundColor = R.color.app_bg,
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
    LazyColumn(modifier) {
            items(viewModel.listens) {
                ListensCard(
                    it,
                    onItemClicked = { listen ->

                    }
                )
            }
    }
}