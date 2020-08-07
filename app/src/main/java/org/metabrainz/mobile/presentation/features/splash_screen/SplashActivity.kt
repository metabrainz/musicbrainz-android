package org.metabrainz.mobile.presentation.features.splash_screen

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.metabrainz.mobile.R
import org.metabrainz.mobile.presentation.UserPreferences
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity
import org.metabrainz.mobile.presentation.features.onboarding.AllowMe

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        val splashImage: ImageView = findViewById(R.id.splash_image)
        splashImage.animation = AnimationUtils.loadAnimation(this,
                R.anim.splashscreen_middle_animation)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (UserPreferences.getOnBoardingStatus())
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity, AllowMe::class.java))
            finish()
        }
    }
}