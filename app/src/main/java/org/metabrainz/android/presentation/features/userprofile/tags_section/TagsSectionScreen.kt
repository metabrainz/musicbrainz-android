package org.metabrainz.android.presentation.features.userprofile.tags_section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import org.metabrainz.android.R
import org.metabrainz.android.theme.ChipTextColor
import org.metabrainz.android.theme.DisabledChipColor
import org.metabrainz.android.theme.EnabledChipColor


@Composable
fun TagsSectionScreen(){
    val constraints = ConstraintSet {
        val chipsSet = createRefFor("chipsSet")
        val predefinedTagSection = createRefFor("genreTypeSection")
        val otherTagSection = createRefFor("otherTypeSection")

        constrain(chipsSet){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(predefinedTagSection){
            top.linkTo(chipsSet.bottom)
            start.linkTo(parent.start)
        }
        constrain(otherTagSection){
            top.linkTo(predefinedTagSection.bottom)
            start.linkTo(parent.start)
        }
    }
    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {

        TagsChipsSection(listOf(UpvotedDownvotedTag.UPVOTE,UpvotedDownvotedTag.DOWNVOTE), layoutID = "chipsSet")
        TagTypeSection("Genres", listOf("Electronic","Experimental","Rock"), layoutID ="genreTypeSection" )
        TagTypeSection("Other Tags", listOf("Cool","Favourite"), layoutID = "otherTypeSection")
    }

}


@Composable
fun TagsChipsSection(
    chips : List<UpvotedDownvotedTag>,
    layoutID:String
){
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow(verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.layoutId(layoutID)) {
        items(chips.size){
            Box(modifier = Modifier
                .padding(start = 15.dp,
                        top = 15.dp,
                        bottom = 15.dp)
                .clickable { selectedChipIndex = it }
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (selectedChipIndex ==it) EnabledChipColor
                    else   DisabledChipColor
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

@Composable
fun ConstraintListItems(items:List<String>){
    ConstraintLayout {
        val cards = createRef()
        LazyListItems(modifier = Modifier.constrainAs(cards){
            start.linkTo(parent.start)
            width = Dimension.fillToConstraints
        },items)
    }
}

@Composable
fun LazyListItems(modifier: Modifier = Modifier,items:List<String>){
    LazyColumn(modifier=modifier.padding(10.dp)){
        items(items.size) {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                    .padding(3.dp),
                elevation = 3.dp,
                shape = RoundedCornerShape(10.dp),
                backgroundColor = colorResource(R.color.dark_gray)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(13.dp)
                ) {
                    Text(items[it])
                }
            }
        }
    }
}

@Composable
fun TagTypeSection(genre: String,
                   items:List<String>,
                   layoutID: String) {
    Column(modifier = Modifier.layoutId(layoutID)) {
        Text(genre, fontSize = 28.sp, modifier = Modifier.padding(start = 16.dp, bottom = 2.dp))
        ConstraintListItems(items)
    }
}


enum class UpvotedDownvotedTag(val value:String, val icon: Int){
    UPVOTE("Upvoted",R.drawable.ic_upvote),
    DOWNVOTE("Downvoted",R.drawable.ic_downvote)
}