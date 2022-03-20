package org.metabrainz.android.presentation.features.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.thefinestartist.finestwebview.FinestWebView
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityDashboardBinding
import org.metabrainz.android.presentation.IntentFactory
import org.metabrainz.android.presentation.features.about.AboutActivity
import org.metabrainz.android.presentation.features.barcode.BarcodeActivity
import org.metabrainz.android.presentation.features.collection.CollectionActivity
import org.metabrainz.android.presentation.features.login.LoginActivity
import org.metabrainz.android.presentation.features.navigation.NavigationItem
import org.metabrainz.android.presentation.features.newsbrainz.NewsBrainzActivity
import org.metabrainz.android.presentation.features.search.SearchActivity
import org.metabrainz.android.presentation.features.tagger.TaggerActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.bottomNav.setContent {
            BottomNavigationBar()
        }

        setSupportActionBar(binding.toolbar)

        //Navigation
        binding.dashboardTagId.setOnClickListener {
            startActivity(Intent(this, TaggerActivity::class.java))
        }
        binding.dashboardAboutId.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        binding.dashboardSearchId.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.dashboardCollectionId.setOnClickListener {
            startActivity(Intent(this, CollectionActivity::class.java))
        }
        binding.dashboardDonateId.setOnClickListener {
            startActivity(Intent(this, DonateActivity::class.java))
        }
        binding.dashboardScanId.setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }

        //CardView animation
        val leftItemAnimation = AnimationUtils.loadAnimation(this, R.anim.left_dashboard_item_animation)
        val rightItemAnimation = AnimationUtils.loadAnimation(this, R.anim.right_dashboard_item_animation)
        binding.dashboardTagId.animation = leftItemAnimation
        binding.dashboardSearchId.animation = rightItemAnimation
        binding.dashboardScanId.animation = rightItemAnimation
        binding.dashboardDonateId.animation = leftItemAnimation
        binding.dashboardAboutId.animation = rightItemAnimation
        binding.dashboardCollectionId.animation = leftItemAnimation

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            binding.dashboardScanId.visibility = GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        menu.findItem(R.id.menu_open_website)?.isVisible = false
        return true
    }

    @Composable
    fun BottomNavigationBar() {
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
                            "news" -> {
                                startActivity(Intent(applicationContext, NewsBrainzActivity::class.java))
                            }
                            "listens" -> {
                                FinestWebView.Builder(applicationContext).show("https://listenbrainz.org/")
                            }
                            "critiques" -> {
                                FinestWebView.Builder(applicationContext).show("https://critiquebrainz.org/")
                            }
                            "profile" -> {
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                            }
                        }
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BottomNavigationBarPreview() {
        BottomNavigationBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_preferences -> {
                startActivity(IntentFactory.getSettings(this))
                true
            }
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}