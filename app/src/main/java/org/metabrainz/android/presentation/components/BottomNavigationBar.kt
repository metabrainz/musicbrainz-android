package org.metabrainz.android.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.thefinestartist.finestwebview.FinestWebView
import org.metabrainz.android.R
import org.metabrainz.android.presentation.features.dashboard.DashboardActivity
import org.metabrainz.android.presentation.features.listens.ListensActivity
import org.metabrainz.android.presentation.features.login.LoginActivity
import org.metabrainz.android.presentation.features.navigation.NavigationItem
import org.metabrainz.android.presentation.features.newsbrainz.NewsBrainzActivity
import org.metabrainz.android.presentation.features.spotify.RemotePlayerKotActivity

@Composable
fun BottomNavigationBar(activity: Activity) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.News,
        NavigationItem.Listens,
        NavigationItem.Critiques,
        NavigationItem.Profile,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.app_bg),
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title, tint = Color.Unspecified) },
                label = { Text(text = item.title) },
                selectedContentColor = colorResource(id = R.color.white),
                unselectedContentColor = colorResource(id = R.color.gray),
                alwaysShowLabel = true,
                selected = true,
                onClick = {
                    when(item.route){
                        "home" -> {
                            val nextActivity = DashboardActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, DashboardActivity::class.java))
                            }
                        }
                        "news" -> {
                            val nextActivity = NewsBrainzActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, RemotePlayerKotActivity::class.java))
                            }
                        }
                        "listens" -> {
                            val nextActivity = ListensActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, ListensActivity::class.java))
                            }
                        }
                        "critiques" -> {
                            FinestWebView.Builder(activity).show("https://critiquebrainz.org/")
                        }
                        "profile" -> {
                            val nextActivity = LoginActivity::class.java
                            if(nextActivity != activity::class.java){
                                activity.startActivity(Intent(activity, LoginActivity::class.java))
                            }
                        }
                    }
                }
            )
        }
    }
}