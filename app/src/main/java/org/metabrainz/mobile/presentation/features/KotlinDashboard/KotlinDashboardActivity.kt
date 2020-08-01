package org.metabrainz.mobile.presentation.features.KotlinDashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.KotlinDashboardLayoutBinding
import org.metabrainz.mobile.presentation.features.about.AboutActivity
import org.metabrainz.mobile.presentation.features.barcode.BarcodeActivity
import org.metabrainz.mobile.presentation.features.collection.CollectionActivity
import org.metabrainz.mobile.presentation.features.dashboard.DonateActivity
import org.metabrainz.mobile.presentation.features.login.LoginActivity
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences
import org.metabrainz.mobile.presentation.features.login.LogoutActivity
import org.metabrainz.mobile.presentation.features.search.SearchActivity
import org.metabrainz.mobile.presentation.features.search.kotlinSearchActivity
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity
import org.metabrainz.mobile.presentation.features.taggerkotlin.KotlinTaggerAcitivty

class KotlinDashboardActivity: AppCompatActivity(){
    private lateinit var binding: KotlinDashboardLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?)
     {
        super.onCreate(savedInstanceState)
         binding = KotlinDashboardLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

         //showing the title only when collapsed
         var isShow = true
         var scrollRange = -1
         binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
             if (scrollRange == -1){
                 scrollRange = barLayout?.totalScrollRange!!
             }
             if (scrollRange + verticalOffset == 0){
                 binding.colToolbarId.title = "MusicBrainz"
                 isShow = true
             } else if (isShow){
                 binding.colToolbarId.title = " "
                 isShow = false
             }
         })
        setSupportActionBar(binding.toolbar)

        //binding = DataBindingUtil.setContentView(this, R.layout.kotlin_dashboard_layout)

        //navigation
        binding.dashboardTagId.setOnClickListener {
            startActivity(Intent(this, KotlinTaggerAcitivty::class.java))
        }
        binding.dashboardAboutId.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
         binding.dashboardSearchId.setOnClickListener {
             startActivity(Intent(this, kotlinSearchActivity::class.java))
         }
        binding.dashboardCollectionId.setOnClickListener {
            startActivity(Intent(this, CollectionActivity::class.java))
        }
        binding.dashboardDonateId.setOnClickListener {
            startActivity(Intent(this,DonateActivity::class.java))
//            startActivity(Intent(this,DonateActivity::class.java))
        }
        binding.dashboardScanId.setOnClickListener {
            startActivity(Intent(this,BarcodeActivity::class.java))
        }



         //cardview animation
         var LeftItemAnimation = AnimationUtils.loadAnimation(this,R.anim.left_dashboard_item_animation)
         var RightItemAnimation = AnimationUtils.loadAnimation(this,R.anim.right_dashboard_item_animation)
         binding.dashboardTagId.animation = LeftItemAnimation
         binding.dashboardSearchId.animation = RightItemAnimation
         binding.dashboardScanId.animation = RightItemAnimation
         binding.dashboardDonateId.animation = LeftItemAnimation
         binding.dashboardAboutId.animation = RightItemAnimation
         binding.dashboardCollectionId.animation = LeftItemAnimation

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_login -> {
                if(LoginSharedPreferences.getLoginStatus()== LoginSharedPreferences.STATUS_LOGGED_OUT)
                    startActivity(Intent(kotlinSearchActivity@this, LoginActivity::class.java))
                else
                    startActivity(Intent(kotlinSearchActivity@this, LogoutActivity::class.java))
                true
            }
            R.id.menu_preferences -> {
                startActivity(Intent(kotlinSearchActivity@this, SettingsActivity::class.java))
            }
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}