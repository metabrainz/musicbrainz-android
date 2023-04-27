package org.metabrainz.android.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.metabrainz.android.R
import org.metabrainz.android.ui.screens.login.LoginActivity
import org.metabrainz.android.ui.navigation.NavigationItem
import org.metabrainz.android.ui.screens.dashboard.DashboardActivity

@Composable
fun BottomNavigationBar(activity: Activity) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Profile,
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon),
                    modifier = Modifier.size(28.dp), contentDescription = item.title, tint = Color.Unspecified) },
                label = { Text(text = item.title, fontSize = 11.sp) },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
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