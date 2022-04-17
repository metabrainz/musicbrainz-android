package org.metabrainz.android.presentation.features.listens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.metabrainz.android.data.sources.api.entities.listens.Listen
import org.metabrainz.android.ui.component.ItemDogCard

class ListensActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Home(listens = FakeListenDatabase.listensList)
        }
    }
}

@Composable
fun Home(
    listens: List<Listen>,
) {
    LazyColumn {
        item {
            //TopBar()
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(listens) {
            listens.forEach {
                ItemDogCard(
                    it,
                    onItemClicked = { listen ->

                    }
                )
            }
        }
    }
}