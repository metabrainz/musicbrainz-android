package org.metabrainz.mobile.presentation.features.search

import android.content.Intent
import android.net.Uri.parse
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.metabrainz.mobile.R
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.databinding.ActivitySearchBinding
import org.metabrainz.mobile.presentation.IntentFactory
import java.net.URI

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        binding.addArtist.setOnClickListener {
            val url = Constants.ADD_ARTIST
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(url)
            startActivity(intent)
        }

        binding.addRelease.setOnClickListener {
            val url = Constants.ADD_RELEASE
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(url)
            startActivity(intent)
        }
        binding.addEvent.setOnClickListener {
            val url = Constants.ADD_EVENT
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(url)
            startActivity(intent)
        }
        binding.addReleaseGroup.setOnClickListener {
            val url = Constants.ADD_RELEASEGROUP
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(url)
            startActivity(intent)
        }
        binding.addLabel.setOnClickListener {
            val url = Constants.ADD_LABEL
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(url)
            startActivity(intent)
        }

        binding.addRecording.setOnClickListener {
            val url = "https://musicbrainz.org/recording/create?edit-recording"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(url)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        menu?.findItem(R.id.menu_open_website)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login -> {
                startActivity(IntentFactory.getLogin(this))
                true
            }
            R.id.menu_preferences -> {
                startActivity(IntentFactory.getSettings(this))
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
//1.Add Artist:   https://musicbrainz.org/artist/create?edit-artist2.Add Release: https://musicbrainz.org/release/add3.Add Event: https://musicbrainz.org/event/create?edit-event4.Add Release group:https://musicbrainz.org/release-group/create?edit-release-group5.Add label: https://musicbrainz.org/label/create?edit-label6.Add recording: https://musicbrainz.org/recording/create?edit-recording