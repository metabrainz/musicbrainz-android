package org.metabrainz.mobile.presentation.features.search

import android.app.SearchManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import org.metabrainz.mobile.App
import org.metabrainz.mobile.R
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.databinding.ActivitySearchResultsBinding
import org.metabrainz.mobile.presentation.features.adapters.ResultItem
import org.metabrainz.mobile.presentation.features.adapters.ResultItemComparator
import org.metabrainz.mobile.presentation.features.adapters.ResultPagingAdapter
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionProvider

/**
 * Activity to display a list of search results to the user and support intents
 * to info Activity types based on the selection.
 */
class SearchResultsActivity : MusicBrainzActivity(), SearchView.OnQueryTextListener {
    private var binding: ActivitySearchResultsBinding? = null
    private var viewModel: SearchViewModel? = null
    private var searchView: SearchView? = null
    private var adapter: ResultPagingAdapter? = null
    private var query: String? = null
    private var entity: MBEntityType? = null
    private var suggestionHelper: SuggestionHelper? = null
    private var suggestionAdapter: CursorAdapter? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setupToolbar(binding)
        query = intent.getStringExtra(SearchManager.QUERY)
        entity = intent.getSerializableExtra(Constants.TYPE) as MBEntityType?
        adapter = ResultPagingAdapter(ResultItemComparator(), entity!!)
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        doSearch()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_view).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.setQuery(query, false)
        searchView!!.isSubmitButtonEnabled = true
        searchView!!.setOnQueryTextListener(this)
        suggestionHelper = SuggestionHelper(this)
        suggestionAdapter = suggestionHelper!!.adapter
        searchView!!.suggestionsAdapter = suggestionAdapter
        searchView!!.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = suggestionAdapter!!.getItem(position) as Cursor
                val query = cursor.getString(cursor.getColumnIndexOrThrow("display1"))
                searchView!!.setQuery(query, false)
                return true
            }
        })
        return true
    }

    private fun doSearch() {
        saveSearchSuggestion(query)
        adapter!!.resetAnimation()
        viewModel!!.search(entity, query)
                .observe(this, { pagingData: PagingData<ResultItem> -> adapter!!.submitData(lifecycle, pagingData) })
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if (!this.query.equals(query, ignoreCase = true)) {
            this.query = query
            doSearch()
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        suggestionAdapter!!.changeCursor(suggestionHelper!!.getMatchingEntries(newText))
        return false
    }

    private fun saveSearchSuggestion(query: String?) {
        val suggestions = SearchRecentSuggestions(this,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE)
        suggestions.saveRecentQuery(query, null)
    }

    override fun getBrowserURI(): Uri {
        return Uri.parse(App.WEBSITE_BASE_URL + "search?type=" + entity!!.name + "&query=" + query)
    }
}