package org.metabrainz.android.presentation.features.about

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import org.metabrainz.android.App
import org.metabrainz.android.R
import org.metabrainz.android.databinding.ActivityAboutBinding
import org.metabrainz.android.presentation.features.base.MusicBrainzActivity

class AboutActivity : MusicBrainzActivity() {
    private var binding: ActivityAboutBinding? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Find this at assets/about.html in the main app folder and display the html content here.
        binding!!.aboutText.setAsset("about.html")

        //Fetch the latest app version and display it.
        val version = getText(R.string.version_text).toString() + " " + App.version
        binding!!.versionText.text = version
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.fragment_about, menu)
        return true
    }

    override fun getBrowserURI(): Uri {
        return Uri.EMPTY
    }
}