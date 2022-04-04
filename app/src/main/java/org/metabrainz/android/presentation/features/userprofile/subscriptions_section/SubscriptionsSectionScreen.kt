package org.metabrainz.android.presentation.features.userprofile.subscriptions_section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.metabrainz.android.presentation.features.userprofile.ChipTextColor
import org.metabrainz.android.presentation.features.userprofile.DisabledChipColor
import org.metabrainz.android.presentation.features.userprofile.EnabledChipColor

@Composable
fun SubscriptionChipSection(
    subscriptionEntities: List<String>,
    layoutID: String
){
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.layoutId(layoutID)) {
        items(subscriptionEntities.size){
            Box(modifier = Modifier
                .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                .clickable { selectedChipIndex = it }
                .clip(RoundedCornerShape(30.dp))
                .background(
                    if (selectedChipIndex ==it) EnabledChipColor
                    else   DisabledChipColor
                )
                .padding(12.dp)){
                Row {
                    Text(text = subscriptionEntities[it],
                         color = ChipTextColor,
                         modifier = Modifier.padding(end = 4.dp),
                         fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SubscriptionsCards(
    subscriptions:List<String>,
    layoutID: String
){
    LazyColumn(modifier=Modifier.padding(10.dp).layoutId(layoutID)){
        items(subscriptions.size){
            Card(modifier = Modifier.fillMaxWidth()
                .height(60.dp)
                .padding(3.dp),
                elevation = 3.dp,
                shape = RoundedCornerShape(10.dp),
                backgroundColor = colorResource(org.metabrainz.android.R.color.dark_gray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(13.dp)) {
                    Text(subscriptions[it])
                }

            }

        }
    }
}

