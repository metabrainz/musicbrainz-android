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
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        var splashImage:ImageView = findViewById(R.id.splash_image)
        var splashText:TextView = findViewById(R.id.splash_text)

        var ImageAnimation = AnimationUtils.loadAnimation(this,R.anim.splashscreen_middle_animation)
        var TextAnimation = AnimationUtils.loadAnimation(this, R.anim.splashscreen_bottom_animation)

        splashImage.animation = ImageAnimation
        splashText.animation = TextAnimation
        //4second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            //finish this activity
            finish()
        },4000)

    }
}