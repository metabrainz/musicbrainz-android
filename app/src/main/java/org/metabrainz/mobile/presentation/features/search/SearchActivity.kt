package org.metabrainz.mobile.presentation.features.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri.parse
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import com.google.android.material.chip.Chip
import org.metabrainz.mobile.R
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.databinding.ActivitySearchBinding
import org.metabrainz.mobile.presentation.IntentFactory
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var suggestionHelper: SuggestionHelper? = null
    private var suggestionAdapter: CursorAdapter? = null

    private lateinit var binding: ActivitySearchBinding
    private var search_index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        setupSearchView()

        suggestionHelper = SuggestionHelper(this)
        suggestionAdapter = suggestionHelper!!.adapter
        binding.searchView.suggestionsAdapter = suggestionAdapter

        binding.cardArtist.setOnClickListener {
            performAction(Constants.ADD_ARTIST)
        }

        binding.cardRelease.setOnClickListener {
            performAction(Constants.ADD_RELEASE)
        }
        binding.cardEvent.setOnClickListener {
            performAction(Constants.ADD_EVENT)
        }
        binding.cardReleaseGroup.setOnClickListener {
            performAction(Constants.ADD_RELEASEGROUP)
        }
        binding.cardLabel.setOnClickListener {
            performAction(Constants.ADD_LABEL)
        }

        binding.cardRecording.setOnClickListener {
            performAction(Constants.ADD_RECORDING)
        }

        val textArray = resources.getStringArray(R.array.searchType)
        for ((index, text) in textArray.withIndex()) {
            val chip = layoutInflater.inflate(R.layout.cat_chip_group_item_choice, binding.chipGroup, false) as Chip
            chip.text = text
            chip.isCloseIconVisible = false
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    search_index = index
                }
            }
            binding.chipGroup.addView(chip)
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

    private fun performAction(url: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = parse(url)
        startActivity(intent)
    }

    private fun startSearch() {
        val query = binding.searchView.query.toString()
        if (query.isNotEmpty()) {
            val searchIntent = Intent(this, SearchResultsActivity::class.java)
            searchIntent.putExtra(SearchManager.QUERY, query)
            searchIntent.putExtra(Constants.TYPE, searchTypeFromSpinner)
            startActivity(searchIntent)
        } else {
            Toast.makeText(this, R.string.toast_search_err, Toast.LENGTH_SHORT).show()
        }
    }

    private val searchTypeFromSpinner: MBEntityType? get() {
        return when (search_index) {
            0 -> MBEntityType.ARTIST
            1 -> MBEntityType.RELEASE
            2 -> MBEntityType.LABEL
            3 -> MBEntityType.RECORDING
            4 -> MBEntityType.EVENT
            5 -> MBEntityType.RELEASE_GROUP
            6 -> MBEntityType.INSTRUMENT
            else -> null
        }
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.isSubmitButtonEnabled = true
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = suggestionAdapter!!.getItem(position) as Cursor
                val query = cursor.getString(cursor.getColumnIndexOrThrow("display1"))
                binding.searchView.setQuery(query, false)
                return true
            }
        })
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        startSearch()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        suggestionAdapter!!.changeCursor(suggestionHelper!!.getMatchingEntries(newText))
        return false
    }
}
//1.Add Artist:   https://musicbrainz.org/artist/create?edit-artist2.Add Release: https://musicbrainz.org/release/add3.Add Event: https://musicbrainz.org/event/create?edit-event4.Add Release group:https://musicbrainz.org/release-group/create?edit-release-group5.Add label: https://musicbrainz.org/label/create?edit-label6.Add recording: https://musicbrainz.org/recording/create?edit-recording