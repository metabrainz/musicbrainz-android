package org.metabrainz.mobile.presentation.features.newsbrainz

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.ActivityNewsbrainzBinding
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity

class NewsBrainzActivity : MusicBrainzActivity() {
    private var binding: ActivityNewsbrainzBinding? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsbrainzBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about, menu)
        return true
    }
}