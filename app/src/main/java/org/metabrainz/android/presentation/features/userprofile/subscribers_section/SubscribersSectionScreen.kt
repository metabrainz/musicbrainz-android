package org.metabrainz.android.presentation.features.userprofile.subscribers_section

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.metabrainz.android.R

@Composable
fun SubscribersSectionScreen() {
    val name = "YellowHatpro"
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            SubscriberCard(name)
        }
    }
}

@Composable
fun SubscriberCard(name: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth()
            .height(60.dp),
        elevation = 3.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = colorResource(R.color.dark_gray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(13.dp)
        ) {
            Text(name)
        }
    }
}