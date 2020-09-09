package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.view.Menu
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.ActivityTaggerBinding
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity

@AndroidEntryPoint
class TaggerActivity : MusicBrainzActivity() {

    private lateinit var binding: ActivityTaggerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaggerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar(binding)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about, menu)
        return true
    }

}