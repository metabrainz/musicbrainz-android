package org.metabrainz.android.presentation.features.userprofile.subscribers_section

import androidx.compose.foundation.layout.*
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
fun MockCard(){
    Row(modifier = Modifier.fillMaxWidth()
        .height(30.dp)
        .padding(12.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp,
            shape = RoundedCornerShape(10.dp),
            backgroundColor = colorResource(R.color.dark_gray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(13.dp)) {
                Text("YellowHatpro")
            }

        }
    }
}