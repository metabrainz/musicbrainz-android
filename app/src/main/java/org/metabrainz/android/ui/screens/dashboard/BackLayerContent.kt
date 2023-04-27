package org.metabrainz.android.ui.screens.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.metabrainz.android.R
import org.metabrainz.android.ui.screens.search.SearchActivity
import org.metabrainz.android.ui.theme.mb_orange
import org.metabrainz.android.ui.theme.mb_purple
import org.metabrainz.android.ui.screens.collection.CollectionActivity
import org.metabrainz.android.ui.screens.newsbrainz.NewsBrainzActivity

@Composable
fun BackLayerContent(activity: Activity, applicationContext: Context, padding: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(padding)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
    ) {
        Image(
            modifier = Modifier
                .size(260.dp, 260.dp)
                .padding(20.dp),
            painter = painterResource(id = R.drawable.ic_musicbrainz_logo_no_text),
            contentDescription = "MusicBrainz",
            contentScale = ContentScale.Fit
        )

        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = mb_purple
                    )
                ) {
                    append("Music")
                }

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = mb_orange
                    )
                ) {
                    append("Brainz")
                }
            },
            fontSize = 55.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = {
                    activity.startActivity(
                        Intent(
                            applicationContext,
                            SearchActivity::class.java
                        )
                    )
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
                    activity.startActivity(
                        Intent(
                            applicationContext,
                            CollectionActivity::class.java
                        )
                    )
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = {
                    activity.startActivity(
                        Intent(
                            applicationContext,
                            NewsBrainzActivity::class.java
                        )
                    )
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
                    painter = painterResource(id = R.drawable.ic_news),
                    alignment = Alignment.CenterStart,
                    contentDescription = "",
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        text = "News",
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = MaterialTheme.colors.surface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.news_card),
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = MaterialTheme.colors.surface,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}