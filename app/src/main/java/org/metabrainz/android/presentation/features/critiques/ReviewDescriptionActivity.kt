package org.metabrainz.android.presentation.features.critiques

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.api.entities.critiques.Review
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.RatingBar
import org.metabrainz.android.presentation.components.TopAppBar

class ReviewDescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val reviewString = intent.extras?.getString("review")
            val review = Gson().fromJson(reviewString, Review::class.java)
            Scaffold(
                backgroundColor = colorResource(id = R.color.app_bg),
                topBar = {
                    TopAppBar(
                        activity = this,
                        title = "Review Details"
                    )
                },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) {
                if (review != null) ReviewDescriptionScreen(review = review)
            }
        }
    }
}
@Composable
fun ReviewDescriptionScreen(review: Review?) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.onSurface
        ) {
            Text(
                text = "${review?.entity_id} by ${review?.user?.display_name}",
                fontSize = 20.sp,
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colors.surface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
            )
        }
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Rating:  ",
                color = colorResource(id = R.color.white),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            RatingBar(rating = review!!.rating, modifier = Modifier.height(30.dp))
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 12.dp, bottom = 20.dp, end = 12.dp)
        ) {
            review?.text?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.white),
                )
            }
        }
    }
}