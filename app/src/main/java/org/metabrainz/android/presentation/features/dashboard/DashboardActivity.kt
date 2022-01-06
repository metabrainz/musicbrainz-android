package org.metabrainz.android.presentation.features.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.thefinestartist.finestwebview.FinestWebView
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityDashboardBinding
import org.metabrainz.android.presentation.IntentFactory
import org.metabrainz.android.presentation.UserPreferences.advancedFeaturesPreference
import org.metabrainz.android.presentation.features.about.AboutActivity
import org.metabrainz.android.presentation.features.barcode.BarcodeActivity
import org.metabrainz.android.presentation.features.collection.CollectionActivity
import org.metabrainz.android.presentation.features.search.SearchActivity
import org.metabrainz.android.presentation.features.tagger.TaggerActivity
import android.content.pm.PackageManager




class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //showing the title only when collapsed
        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.colToolbarId.title = "MusicBrainz"
                binding.colToolbarId.setCollapsedTitleTextColor(resources.getColor(R.color.white))
                isShow = true
            } else if (isShow) {
                binding.colToolbarId.title = " "
                isShow = false
            }
        })
        setSupportActionBar(binding.toolbar)

        //navigation
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
        binding.dashboardListenId.setOnClickListener {
            FinestWebView.Builder(this).show("https://listenbrainz.org/")
        }
        binding.dashboardCritiqueId.setOnClickListener {
            FinestWebView.Builder(this).show("https://critiquebrainz.org/")
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
        binding.dashboardCritiqueId.animation = leftItemAnimation
        binding.dashboardListenId.animation = rightItemAnimation


        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            binding.dashboardScanId.visibility = GONE
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            advancedFeaturesPreference -> {
                binding.advancedFeatures.visibility = VISIBLE
            }
            else -> {
                binding.advancedFeatures.visibility = GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        menu.findItem(R.id.menu_open_website)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login -> {
                startActivity(IntentFactory.getLogin(this))
                true
            }
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