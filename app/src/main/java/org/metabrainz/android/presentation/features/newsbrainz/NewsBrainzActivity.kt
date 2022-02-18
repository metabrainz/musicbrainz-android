package org.metabrainz.android.presentation.features.newsbrainz

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.CursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.databinding.ActivityNewsbrainzBinding
import org.metabrainz.android.presentation.IntentFactory
import org.metabrainz.android.presentation.features.adapters.BlogAdapter
import org.metabrainz.android.presentation.features.adapters.ResultItemComparator
import org.metabrainz.android.presentation.features.adapters.ResultPagingAdapter
import org.metabrainz.android.presentation.features.suggestion.SuggestionHelper

@AndroidEntryPoint
class NewsBrainzActivity : AppCompatActivity() {

    private var suggestionHelper: SuggestionHelper? = null
    private var suggestionAdapter: CursorAdapter? = null

    private lateinit var binding: ActivityNewsbrainzBinding
    private var viewModel: NewsListViewModel? = null
    private var adapter: BlogAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsbrainzBinding.inflate(layoutInflater)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        suggestionHelper = SuggestionHelper(this)
        suggestionAdapter = suggestionHelper!!.adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this)[NewsListViewModel::class.java]

        adapter = BlogAdapter()
        binding.recyclerView.adapter = adapter
        adapter!!.resetAnimation()
        viewModel!!.fetchBlogs().observe(this) {
            adapter.submitData()
            Log.d("njaif",it.posts[0].title)
        }
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