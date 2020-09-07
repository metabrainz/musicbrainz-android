package org.metabrainz.mobile.presentation.features.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.OnboardMainBinding
import org.metabrainz.mobile.presentation.UserPreferences
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity

class GettingStarted : AppCompatActivity() {

    lateinit var binding: OnboardMainBinding
    lateinit var adapter: SliderAdapter
    var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SliderAdapter()
        binding.pager.adapter = adapter
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.tabs, binding.pager) { _, _ -> }.attach()

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                when (position) {
                    0 -> {
                        binding.prevB.visibility = View.INVISIBLE
                        binding.nextB.setText(R.string.next)
                    }
                    adapter.itemCount - 1 -> {
                        binding.prevB.visibility = View.VISIBLE
                        binding.nextB.setText(R.string.finish)
                    }
                    else -> {
                        binding.prevB.visibility = View.VISIBLE
                        binding.nextB.setText(R.string.next)
                    }
                }
            }
        })

        binding.nextB.setOnClickListener {
            if (currentPage == adapter.itemCount - 1) {
                UserPreferences.setOnBoardingCompleted()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else binding.pager.currentItem = currentPage + 1
        }

        binding.prevB.setOnClickListener { binding.pager.currentItem = currentPage - 1 }
    }

}