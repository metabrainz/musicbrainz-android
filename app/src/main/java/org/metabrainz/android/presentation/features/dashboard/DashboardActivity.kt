package org.metabrainz.android.presentation.features.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.presentation.components.BottomNavigationBar
import org.metabrainz.android.presentation.components.TopAppBar
import org.metabrainz.android.presentation.features.collection.CollectionActivity
import org.metabrainz.android.presentation.features.onboarding.FeaturesActivity
import org.metabrainz.android.presentation.features.search.SearchActivity
import org.metabrainz.android.presentation.features.tagger.TaggerActivity

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("onboarding", false)) {
            startActivity(Intent(this, FeaturesActivity::class.java))
            finish()
        }

        setContent {
            Scaffold(
                backgroundColor = colorResource(id = R.color.app_bg),
                topBar = { TopAppBar(activity = this, title = "Home") },
                bottomBar = { BottomNavigationBar(activity = this) }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .padding(it)
                ) {
                    Image(
                        modifier = Modifier
                            .size(230.dp, 230.dp)
                            .padding(20.dp),
                        painter = painterResource(id = R.drawable.ic_musicbrainz_logo_no_text),
                        contentDescription = "MusicBrainz",
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
                                color = colorResource(
                                    id = R.color.mb_purple))) {
                                append("Music")
                            }

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
                                color = colorResource(
                                    id = R.color.mb_orange))) {
                                append("Brainz")
                            }
                        },
                        fontSize = 45.sp,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = {
                                startActivity(Intent(applicationContext,
                                    SearchActivity::class.java))
                            }),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.onSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(80.dp, 80.dp)
                                    .padding(4.dp),
                                painter = painterResource(id = R.drawable.ic_search),
                                alignment = Alignment.CenterStart,
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                Text(
                                    text = "Search",
                                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                                    color = MaterialTheme.colors.surface,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(id = R.string.search_card),
                                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                                    color = MaterialTheme.colors.surface,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = {
                                startActivity(Intent(applicationContext,
                                    TaggerActivity::class.java))
                            }),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.onSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(80.dp, 80.dp)
                                    .padding(4.dp),
                                painter = painterResource(id = R.drawable.ic_music_note),
                                alignment = Alignment.CenterStart,
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                Text(
                                    text = "Tag",
                                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                                    color = MaterialTheme.colors.surface,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(id = R.string.tagger_card),
                                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                                    color = MaterialTheme.colors.surface,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = {
                                startActivity(Intent(applicationContext,
                                    CollectionActivity::class.java))
                            }),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.onSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(80.dp, 80.dp)
                                    .padding(4.dp),
                                painter = painterResource(id = R.drawable.ic_collection),
                                alignment = Alignment.CenterStart,
                                contentDescription = "",
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                Text(
                                    text = "Collection",
                                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                                    color = MaterialTheme.colors.surface,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(id = R.string.collection_card),
                                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                                    color = MaterialTheme.colors.surface,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}