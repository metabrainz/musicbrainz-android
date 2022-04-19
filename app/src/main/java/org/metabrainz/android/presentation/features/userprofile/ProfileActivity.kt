package org.metabrainz.android.presentation.features.userprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.metabrainz.android.R
import org.metabrainz.android.presentation.features.dashboard.DashboardActivity
import org.metabrainz.android.presentation.features.login.LoginSharedPreferences
import org.metabrainz.android.presentation.features.settings.SettingsActivity
import org.metabrainz.android.presentation.features.userprofile.collections_section.CollectionsSectionScreen
import org.metabrainz.android.presentation.features.userprofile.profile_section.ProfileSectionScreen
import org.metabrainz.android.presentation.features.userprofile.ratings_section.RatingsSectionScreen
import org.metabrainz.android.presentation.features.userprofile.subscribers_section.SubscribersSectionScreen
import org.metabrainz.android.presentation.features.userprofile.subscriptions_section.SubscriptionSectionScreen
import org.metabrainz.android.presentation.features.userprofile.tags_section.TagsSectionScreen
import org.metabrainz.android.theme.Theme

class ProfileActivity : AppCompatActivity() {
    private val showDialog = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                TopAppBar()
                TabBar()
                if (showDialog.value) LogOutDialog()
                TopAppBar()
            }
        }
    }

    @Composable
    fun TopAppBar() {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    startActivity(
                        Intent(
                            applicationContext,
                            DashboardActivity::class.java
                        )
                    )
                }) {
                    Icon(Icons.Filled.ArrowBack, "Go Back")
                }
            },
            title = {
                Text(text = "User Profile")
            },
            backgroundColor = colorResource(id = R.color.app_bg),
            contentColor = colorResource(id = R.color.white),
            elevation = 2.dp,
            actions = {

                IconButton(onClick = {
                    showDialog.value = true
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_user),
                        "Log Out",
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                }) {
                    Icon(
                        painterResource(id = R.drawable.action_settings),
                        "Settings",
                        tint = Color.Unspecified
                    )
                }
            }
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun TabBar() {
        val tabdata = listOf(
            ProfileTabItem.Profile,
            ProfileTabItem.Subscriptions,
            ProfileTabItem.Subscribers,
            ProfileTabItem.Collection,
            ProfileTabItem.Tags,
            ProfileTabItem.Ratings
        )
        val pagerState = rememberPagerState(
            initialPage = 0
        )
        val tabIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()
        Column {
            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                edgePadding = TabRowDefaults.ScrollableTabRowPadding,
                backgroundColor = colorResource(R.color.dark_gray),
                indicator = @Composable { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[tabIndex])
                    )
                }
            ) {
                tabdata.forEachIndexed { index, listItem ->
                    Tab(selected = tabIndex == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(text = listItem.title, fontSize = 16.sp)
                    }, unselectedContentColor = colorResource(R.color.app_bg_light),
                        selectedContentColor = colorResource(R.color.white)
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                count = tabdata.size
            ) { page ->
                Theme {
                    Surface {
                        tabdata[page].screen()
                    }
                }
            }
        }
    }

    private fun logoutUser() {
        LoginSharedPreferences.logoutUser()
        Toast.makeText(
            applicationContext,
            "User has successfully logged out.",
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    @Composable
    fun LogOutDialog() {
        if (showDialog.value) {
            AlertDialog(
                backgroundColor = colorResource(R.color.dark_gray),
                onDismissRequest = {
                    showDialog.value = false
                },
                title = {
                    Text(
                        text = "Log Out",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white)
                    )
                },
                text = {
                    Text(
                        fontSize = 16.sp,
                        text = "Confirm Log Out",
                        color = colorResource(R.color.white)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog.value = false
                            logoutUser()
                        }
                    ) {
                        Text(
                            "CONFIRM",
                            fontSize = 15.sp,
                            color = colorResource(R.color.mb_purple_medium)
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog.value = false
                        }
                    ) {
                        Text(
                            "DISMISS",
                            fontSize = 15.sp,
                            color = colorResource(R.color.mb_purple_medium)
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

sealed class ProfileTabItem(var title: String, var screen: @Composable () -> Unit){
    object Profile: ProfileTabItem("Profile", { ProfileSectionScreen() })
    object Subscriptions: ProfileTabItem("Subscriptions", { SubscriptionSectionScreen() })
    object Subscribers: ProfileTabItem("Subscribers", { SubscribersSectionScreen() })
    object Tags: ProfileTabItem("Tags", { TagsSectionScreen() })
    object Collection: ProfileTabItem("Collection",{ CollectionsSectionScreen() })
    object Ratings: ProfileTabItem("Ratings", { RatingsSectionScreen() })
}

