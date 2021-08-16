package org.metabrainz.mobile.presentation.features.splash_screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.metabrainz.mobile.presentation.UserPreferences
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity
import org.metabrainz.mobile.presentation.features.onboarding.FeaturesActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (UserPreferences.onBoardingStatus) {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            }
            else {
                startActivity(Intent(this@SplashActivity, FeaturesActivity::class.java))
            }
            finish()
        }
    }
}