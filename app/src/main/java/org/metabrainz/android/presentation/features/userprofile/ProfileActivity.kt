package org.metabrainz.android.presentation.features.userprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.material.tabs.TabLayoutMediator
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityProfileBinding
import org.metabrainz.android.presentation.features.dashboard.DashboardActivity
import org.metabrainz.android.presentation.features.login.LoginSharedPreferences
import org.metabrainz.android.presentation.features.settings.SettingsActivity

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profileTopAppBar.setContent {
            TopAppBar()
        }

        val pagerAdapter= ProfileViewPagerAdapter(this)
        binding.profilePager.adapter = pagerAdapter
        TabLayoutMediator(binding.profileTabLayout, binding.profilePager){tab,position->
            run{
                when(position){
                    0->tab.text = "Profile"
                    1->tab.text = "Subscriptions"
                    2->tab.text = "Subscribers"
                    3->tab.text = "Collections"
                    4->tab.text = "Tags"
                    5->tab.text = "Ratings"
                }
            }

        }.attach()
    }

    private fun logoutUser() {
        LoginSharedPreferences.logoutUser()
        Toast.makeText(applicationContext,
            "User has successfully logged out.",
            Toast.LENGTH_LONG).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    @Composable
    fun TopAppBar() {
        androidx.compose.material.TopAppBar(
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
                    logoutUser()
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
}