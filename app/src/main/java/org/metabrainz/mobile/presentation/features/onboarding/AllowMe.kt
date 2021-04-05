package org.metabrainz.mobile.presentation.features.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.metabrainz.mobile.databinding.OnboardAllowMeBinding
import org.metabrainz.mobile.presentation.UserPreferences.setOnBoardingCompleted
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity

class AllowMe : AppCompatActivity() {
    private var binding: OnboardAllowMeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardAllowMeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.skipBtn.setOnClickListener {
            setOnBoardingCompleted()
            val intent = Intent(this@AllowMe, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding!!.letsGo.setOnClickListener {
            val intent = Intent(this@AllowMe, GettingStarted::class.java)
            startActivity(intent)
            finish()
        }
    }
}