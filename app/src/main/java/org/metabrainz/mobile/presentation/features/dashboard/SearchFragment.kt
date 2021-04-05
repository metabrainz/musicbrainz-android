package org.metabrainz.mobile.presentation.features.dashboard

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.fragment.app.Fragment
import org.metabrainz.mobile.R
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.databinding.FragmentDashSearchBinding
import org.metabrainz.mobile.presentation.features.search.SearchResultsActivity
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {
    private var binding: FragmentDashSearchBinding? = null
    private var suggestionHelper: SuggestionHelper? = null
    private var suggestionAdapter: CursorAdapter? = null
    private val clearFocusView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDashSearchBinding.inflate(inflater, container, false)

        //Work around to prevent keyboard from auto showing
        binding!!.clearFocusView.requestFocus()
        setupSearchView()
        binding!!.searchView.setBackgroundResource(R.drawable.searchview_bg)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSearchTypeSpinner()
        suggestionHelper = SuggestionHelper(requireActivity())
        suggestionAdapter = suggestionHelper!!.adapter
        binding!!.searchView.suggestionsAdapter = suggestionAdapter
    }

    private fun setupSearchTypeSpinner() {
        val typeAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.searchType, android.R.layout.simple_spinner_item)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.searchSpin.adapter = typeAdapter
    }

    private fun startSearch() {
        val query = binding!!.searchView.query.toString()
        if (query.length > 0) {
            val searchIntent = Intent(activity, SearchResultsActivity::class.java)
            searchIntent.putExtra(SearchManager.QUERY, query)
            searchIntent.putExtra(Constants.TYPE, searchTypeFromSpinner)
            startActivity(searchIntent)
        } else {
            Toast.makeText(activity, R.string.toast_search_err, Toast.LENGTH_SHORT).show()
        }
    }

    private val searchTypeFromSpinner: MBEntityType?
        private get() {
            val spinnerPosition = binding!!.searchSpin.selectedItemPosition
            return when (spinnerPosition) {
                0 -> MBEntityType.ARTIST
                1 -> MBEntityType.RELEASE
                2 -> MBEntityType.LABEL
                3 -> MBEntityType.RECORDING
                4 -> MBEntityType.RELEASE_GROUP
                5 -> MBEntityType.INSTRUMENT
                6 -> MBEntityType.EVENT
                else -> null
            }
        }

    private fun setupSearchView() {
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding!!.searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        binding!!.searchView.isSubmitButtonEnabled = true
        binding!!.searchView.setIconifiedByDefault(false)
        binding!!.searchView.setOnQueryTextListener(this)
        binding!!.searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = suggestionAdapter!!.getItem(position) as Cursor
                val query = cursor.getString(cursor.getColumnIndexOrThrow("display1"))
                binding!!.searchView.setQuery(query, false)
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