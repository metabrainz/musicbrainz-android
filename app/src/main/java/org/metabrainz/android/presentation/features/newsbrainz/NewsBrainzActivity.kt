package org.metabrainz.android.presentation.features.newsbrainz

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.CursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.databinding.ActivityNewsbrainzBinding
import org.metabrainz.android.presentation.IntentFactory
import org.metabrainz.android.presentation.features.adapters.ResultItem
import org.metabrainz.android.presentation.features.adapters.ResultItemComparator
import org.metabrainz.android.presentation.features.adapters.ResultPagingAdapter
import org.metabrainz.android.presentation.features.search.SearchViewModel
import org.metabrainz.android.presentation.features.suggestion.SuggestionHelper

class NewsBrainzActivity : AppCompatActivity(){

    private var suggestionHelper: SuggestionHelper? = null
    private var suggestionAdapter: CursorAdapter? = null

    private lateinit var binding: ActivityNewsbrainzBinding
    private var viewModel: SearchViewModel? = null
    private var adapter: ResultPagingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsbrainzBinding.inflate(layoutInflater)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        suggestionHelper = SuggestionHelper(this)
        suggestionAdapter = suggestionHelper!!.adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        adapter = ResultPagingAdapter(ResultItemComparator(), MBEntityType.ARTIST)
        binding.recyclerView.adapter = adapter
        adapter!!.resetAnimation()
        viewModel!!.search(MBEntityType.ARTIST, "News").observe(this, { pagingData: PagingData<ResultItem> ->
            adapter!!.submitData(lifecycle, pagingData)
        })
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
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