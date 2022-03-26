package org.metabrainz.android.presentation.features.userprofile

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.title = "User Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}