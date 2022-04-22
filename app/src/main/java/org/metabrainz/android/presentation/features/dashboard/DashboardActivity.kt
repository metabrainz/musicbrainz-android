package org.metabrainz.android.presentation.features.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityDashboardBinding
import org.metabrainz.android.presentation.UserPreferences
import org.metabrainz.android.presentation.features.collection.CollectionActivity
import org.metabrainz.android.presentation.features.onboarding.FeaturesActivity
import org.metabrainz.android.presentation.features.search.SearchActivity
import org.metabrainz.android.presentation.features.tagger.TaggerActivity
import org.metabrainz.android.ui.components.BottomNavigationBar
import org.metabrainz.android.ui.components.TopAppBar

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        if (UserPreferences.onBoardingStatus) {
            startActivity(Intent(this, FeaturesActivity::class.java))
            finish()
        }
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.bottomNav.setContent {
            BottomNavigationBar(this)
        }
        binding.topAppBar.setContent {
            TopAppBar(this)
        }

        //Navigation
        binding.dashboardTagId.setOnClickListener {
            startActivity(Intent(this, TaggerActivity::class.java))
        }
        binding.dashboardSearchId.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.dashboardCollectionId.setOnClickListener {
            startActivity(Intent(this, CollectionActivity::class.java))
        }

        //CardView animation
        val leftItemAnimation = AnimationUtils.loadAnimation(this, R.anim.left_dashboard_item_animation)
        val rightItemAnimation = AnimationUtils.loadAnimation(this, R.anim.right_dashboard_item_animation)
        binding.dashboardTagId.animation = leftItemAnimation
        binding.dashboardSearchId.animation = rightItemAnimation
        binding.dashboardCollectionId.animation = leftItemAnimation
    }
}