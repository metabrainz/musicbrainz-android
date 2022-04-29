package org.metabrainz.android.presentation.features.userprofile.profile_section


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import org.metabrainz.android.R

@Composable
fun ProfileSectionScreen() {
    ProfileSection()
}

@Composable
fun ProfileSection() {
    val listOfProfileCards = arrayListOf(
        Pair("Name", "YellowHatPro"),
        Pair("Email", "yellowhatpro3119@gmail.com"),
        Pair("Tags", "12"),
        Pair("Ratings", "1"),
        Pair("Collections", "5"),
        Pair("Subscriptions", "2"),
        Pair("Subscribers", "0")
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth().padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(R.drawable.ic_editor_image),
                    "MusicBrainz Profile",
                    modifier = Modifier
                        .layoutId("mbProfileImage")
                        .size(155.dp)
                )
            }
        }
        itemsIndexed(listOfProfileCards) { _, value ->
            ProfileCard(key = value.first, value = value.second)
        }
    }
}

@Composable
fun ProfileCard(
    key:String,
    value:String
){
    Card(modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 4.dp)
        .fillMaxWidth()
        .height(60.dp),
        elevation = 3.dp,
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

