package org.metabrainz.android.ui.screens.onboarding

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aemerse.onboard.OnboardAdvanced
import com.aemerse.onboard.OnboardFragment
import com.aemerse.onboard.OnboardPageTransformerType
import com.aemerse.onboard.model.SliderPage
import org.metabrainz.android.R
import org.metabrainz.android.ui.screens.dashboard.DashboardActivity
import org.metabrainz.android.util.UserPreferences

class FeaturesActivity : OnboardAdvanced() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(OnboardFragment.newInstance(
            SliderPage(
                "Search",
                "Explore MusicBrainz Data",
                resourceId = R.raw.search,
                backgroundColor =  ContextCompat.getColor(applicationContext, R.color.app_bg),
                titleColor = ContextCompat.getColor(applicationContext, R.color.white),
                descriptionColor = ContextCompat.getColor(applicationContext, R.color.white),
                isLottie = true
            )
        ))

        addSlide(OnboardFragment.newInstance(
            "Scan",
            "Barcodes Search",
            resourceId = R.raw.scan,
            backgroundColor =  ContextCompat.getColor(applicationContext, R.color.app_bg),
            titleColor = ContextCompat.getColor(applicationContext, R.color.white),
            descriptionColor = ContextCompat.getColor(applicationContext, R.color.white),
            isLottie = true
        ))

        addSlide(OnboardFragment.newInstance(
            "Collections",
            "Explore your MusicBrainz Collection",
            resourceId = R.raw.collections,
            backgroundColor =  ContextCompat.getColor(applicationContext, R.color.app_bg),
            titleColor = ContextCompat.getColor(applicationContext, R.color.white),
            descriptionColor = ContextCompat.getColor(applicationContext, R.color.white),
            isLottie = true
        ))

        addSlide(OnboardFragment.newInstance(
            "Cross Platform",
            "Transfer music data across devices",
            resourceId = R.raw.laptop_and_stuff,
            backgroundColor =  ContextCompat.getColor(applicationContext, R.color.app_bg),
            titleColor = ContextCompat.getColor(applicationContext, R.color.white),
            descriptionColor = ContextCompat.getColor(applicationContext, R.color.white),
            isLottie = true
        ))

        setTransformer(OnboardPageTransformerType.Parallax())
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Skip?")
        builder.setMessage("Some of the functionalities are specially available through the settings.")
        builder.setPositiveButton("Understood") { dialog: DialogInterface?, which: Int ->
            UserPreferences.setOnBoardingCompleted()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Cancel") {dialog: DialogInterface?, which: Int ->}
        builder.create().show()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        UserPreferences.setOnBoardingCompleted()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}