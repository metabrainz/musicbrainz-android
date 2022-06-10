package org.metabrainz.android.presentation.features.critiques

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.App
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.api.entities.critiques.Review
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.features.critiques.CritiqueEntity.*
import org.metabrainz.android.presentation.features.listens.Loader

@AndroidEntryPoint
class CritiqueActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                backgroundColor = colorResource(id = R.color.app_bg),
                topBar = {
                    org.metabrainz.android.presentation.components.TopAppBar(
                        activity = this,
                        title = "Critiques"
                    )
                },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    CritiquesMainScreen(this@CritiqueActivity)
                }
            }
        }
    }
}

@Composable
fun CritiquesMainScreen(activity: Activity) {
    val viewModel = hiltViewModel<CritiquesViewModel>()
    Column {
        AnimatedVisibility(
            visible = viewModel.isLoading,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250)),
            modifier = Modifier
                .fillMaxHeight()
        ){
            Loader()
        }
        EntityChips(activity= activity)
        ReviewCards(viewModel = viewModel, activity = activity)
    }
}

@Composable
fun ReviewCards(viewModel: CritiquesViewModel, activity: Activity) {
    viewModel.fetchAllReviews()

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = viewModel.reviews) { item ->
            ReviewCard(
                name = item.user.display_name,
                review = item,
                modifier = Modifier.clickable {
                    val reviewJson = Gson().toJson(item)
                    activity.startActivity(
                        Intent(
                            activity,
                            ReviewDescriptionActivity::class.java
                        ).putExtra("review", reviewJson)
                    )
                }
            )
        }
    }
}

@Composable
fun ReviewCard(review: Review, name:String, modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
        backgroundColor = MaterialTheme.colors.onSurface

    ) {
        Row(modifier = modifier.padding(24.dp)) {
            Column(
                modifier = modifier
                    .weight(1f)
            ) {
                Text(
                    text = review.id,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colors.surface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Review by: $name",
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colors.surface,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun EntityChips(
    viewModel: CritiquesViewModel = viewModel(),
    activity: Activity
) {
    val entities = listOf(AllReviews,Artist,ReleaseGroup, Label, Recording, Event, Place, Work)
    viewModel.fetchAllReviews()
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        items(entities.size) {
            Box(modifier = Modifier
                .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                .clickable {
                    if (App.context?.isOnline == false) {
                        Toast
                            .makeText(
                                activity,
                                "Connect to Internet and Try Again",
                                Toast.LENGTH_LONG
                            )
                            .show()
                        return@clickable
                    }
                    selectedChipIndex = it
                    viewModel.reviews = listOf()
                    viewModel.fetchReviews(entities[it].entity)
                }
                .clip(RoundedCornerShape(30.dp))
                .background(
                    if (selectedChipIndex == it)
                        colorResource(
                            id = R.color.mb_purple_medium
                        )
                    else colorResource(id = R.color.light_gray)
                )
                .padding(12.dp)) {
                Row {
                    Text(
                        text = entities[it].displayName,
                        color = Color.Black,
                        modifier = Modifier.padding(end = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
