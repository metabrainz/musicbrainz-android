package org.metabrainz.mobile.presentation.features.SplashActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.PersistableBundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import org.metabrainz.mobile.R
import org.metabrainz.mobile.presentation.features.KotlinDashboard.KotlinDashboardActivity
import org.metabrainz.mobile.presentation.features.OnBoarding.AllowMe
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity
import org.metabrainz.mobile.presentation.features.login.LoginSharedPreferences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        var splashImage:ImageView = findViewById(R.id.splash_image)

        var ImageAnimation = AnimationUtils.loadAnimation(this,R.anim.splashscreen_middle_animation)
        splashImage.animation = ImageAnimation

        //4second splash time
        Handler().postDelayed({
            if(LoginSharedPreferences.getSkipState() == true )
              startActivity(Intent(this@SplashActivity, KotlinDashboardActivity::class.java))
            else
                startActivity(Intent(this@SplashActivity,AllowMe::class.java))

            //finish this activity
            finish()
        },3000)
    }
}