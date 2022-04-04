package org.metabrainz.android.presentation.features.userprofile.profile_section


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import org.metabrainz.android.R

val CardWidth = Dimension.matchParent
val CardHeight = Dimension.value(60.dp)

@Composable
fun ProfileSectionScreen(){
    ProfileSection()
}

@Composable
fun ProfileSection(){
    val constraints = ConstraintSet {
        val image = createRefFor("mbProfileImage")
        val nameCard = createRefFor("nameCard")
        val mailCard = createRefFor("mailCard")
        val tagsCard = createRefFor("tagsCard")
        val ratingsCard = createRefFor("ratingsCard")
        val collectionsCard = createRefFor("collectionsCard")
        val subscriptionsCard = createRefFor("subscriptionsCard")
        val subscribersCard = createRefFor("subscribersCard")

        constrain(image){
            top.linkTo(parent.top, margin = 12.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.wrapContent
            width = Dimension.wrapContent
        }

        constrain(nameCard){
            top.linkTo(image.bottom, margin = 12.dp)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
        constrain(mailCard){
            top.linkTo(nameCard.bottom)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
        constrain(tagsCard){
            top.linkTo(mailCard.bottom)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
        constrain(ratingsCard){
            top.linkTo(tagsCard.bottom)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
        constrain(collectionsCard){
            top.linkTo(ratingsCard.bottom)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
        constrain(subscriptionsCard){
            top.linkTo(collectionsCard.bottom)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
        constrain(subscribersCard){
            top.linkTo(subscriptionsCard.bottom)
            start.linkTo(parent.start, margin = 12.dp)
            end.linkTo(parent.end, margin = 12.dp)
            width = CardWidth
            height = CardHeight
        }
    }
    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.ic_editor_image),
            "MusicBrainz Profile",
            modifier = Modifier.layoutId("mbProfileImage")
                .size(155.dp))

        ProfileCard("Name","YellowHatPro","nameCard")
        ProfileCard("Email","yellowhatpro3119@gmail.com","mailCard")
        ProfileCard("Tags","12","tagsCard")
        ProfileCard("Ratings","1","ratingsCard")
        ProfileCard("Collections","5","collectionsCard")
        ProfileCard("Subscriptions","2","subscriptionsCard")
        ProfileCard("Subscribers","0","subscribersCard")
    }
}

@Composable
fun ProfileCard(
    key:String,
    value:String,
    layoutId:String,

){
    Card(modifier = Modifier
        .layoutId(layoutId)
        .padding(2.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = colorResource(R.color.dark_gray)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(10.dp)

        ) {
            Text(key, fontWeight = FontWeight.Bold, color = colorResource(R.color.white))
            Text(value,color = colorResource(R.color.white))
        }
    }
}

