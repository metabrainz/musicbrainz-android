package org.metabrainz.android.presentation.features.userprofile.tags_section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.metabrainz.android.R
import org.metabrainz.android.theme.ChipTextColor
import org.metabrainz.android.theme.DisabledChipColor
import org.metabrainz.android.theme.EnabledChipColor

@Composable
fun TagsSectionScreen() {
    val genreList = arrayListOf("Electronic", "Experimental", "Rock")
    val otherTagList = arrayListOf("Cool", "Favourite", "Funny", "Cute", "Random")
    val tags: ArrayList<Pair<String, List<String>>> = arrayListOf(
        Pair("Genre", genreList),
        Pair("Other Tags", otherTagList)
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                TagsChipsSection(listOf(UpvotedDownvotedTag.UPVOTE, UpvotedDownvotedTag.DOWNVOTE))
            }
        }
        itemsIndexed(tags) { _, value ->
            Text(
                value.first,
                fontSize = 28.sp,
                modifier = Modifier.padding(14.dp)
            )
            val lazyColumnHeight = value.second.size * 68 //68 denotes size of each card + padding
            LazyColumn(modifier = Modifier.height((lazyColumnHeight).dp)) {
                items(value.second.size) { index ->
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
                            Text(value.second[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagsChipsSection(
    chips : List<UpvotedDownvotedTag>
){
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center) {
        items(chips.size){
            Box(modifier = Modifier
                .padding(
                    start = 15.dp,
                    top = 15.dp,
                    bottom = 15.dp
                )
                .clickable { selectedChipIndex = it }
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (selectedChipIndex == it) EnabledChipColor
                    else DisabledChipColor
                )
                .padding(12.dp)){
                Row {
                    Text(text = chips[it].value,
                        color = ChipTextColor,
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.Bold)
                    Image(painter = painterResource(chips[it].icon),"")
                }
            }
        }
    }
}

enum class UpvotedDownvotedTag(val value:String, val icon: Int){
    UPVOTE("Upvoted",R.drawable.ic_upvote),
    DOWNVOTE("Downvoted",R.drawable.ic_downvote)
}