package org.metabrainz.android.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.metabrainz.android.R
import org.metabrainz.android.presentation.features.about.AboutActivity
import org.metabrainz.android.presentation.features.dashboard.DonateActivity
import org.metabrainz.android.presentation.features.settings.SettingsActivity

@Composable
fun TopAppBar(activity: Activity) {
    androidx.compose.material.TopAppBar(
        title = {
            Text(text = "MusicBrainz")
        },
        backgroundColor = colorResource(id = R.color.app_bg),
        contentColor = colorResource(id = R.color.white),
        elevation = 2.dp,
        actions = {
            IconButton(onClick = {
                activity.startActivity(Intent(activity, AboutActivity::class.java))
            }) {
                Icon(painterResource(id = R.drawable.ic_information),
                    "About",
                    tint = Color.Unspecified)
            }
            IconButton(onClick = {
                activity.startActivity(Intent(activity, DonateActivity::class.java))
            }) {
                Icon(painterResource(id = R.drawable.ic_donate), "Donate", tint = Color.Unspecified)
            }
            IconButton(onClick = {
                activity.startActivity(Intent(activity, SettingsActivity::class.java))
            }) {
                Icon(painterResource(id = R.drawable.action_settings),
                    "Settings",
                    tint = Color.Unspecified)
            }
        }
    )
}
