package org.metabrainz.mobile.presentation.features.dashboard

import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import org.metabrainz.mobile.R

class DonateActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
        super.onCreate(savedInstanceState)
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(resources.getColor(R.color.colorPrimaryDark))
                .setShowTitle(true)
                .build()
        customTabsIntent.launchUrl(this,
                Uri.parse("https://metabrainz.org/donate"))
    }

    override fun onResume() {
        finish()
        super.onResume()
    }
}