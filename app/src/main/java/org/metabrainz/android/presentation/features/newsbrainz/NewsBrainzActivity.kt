package org.metabrainz.android.presentation.features.newsbrainz

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.CursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.R
import org.metabrainz.android.data.sources.api.entities.blog.Post
import org.metabrainz.android.databinding.ActivityNewsbrainzBinding
import org.metabrainz.android.presentation.IntentFactory
import org.metabrainz.android.presentation.features.adapters.BlogAdapter
import org.metabrainz.android.presentation.features.suggestion.SuggestionHelper


@AndroidEntryPoint
class NewsBrainzActivity : AppCompatActivity(), BlogAdapter.ClickListener {

    private var suggestionHelper: SuggestionHelper? = null
    private var suggestionAdapter: CursorAdapter? = null

    private lateinit var binding: ActivityNewsbrainzBinding
    private var viewModel: NewsListViewModel? = null
    private var adapter: BlogAdapter? = null
    private lateinit var postsList: ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsbrainzBinding.inflate(layoutInflater)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_bg)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "NewsBrainz"
        setContentView(binding.root)

        suggestionHelper = SuggestionHelper(this)
        suggestionAdapter = suggestionHelper!!.adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this)[NewsListViewModel::class.java]

        postsList  = ArrayList()
        adapter = BlogAdapter(this, postsList, this)
        binding.recyclerView.adapter = adapter

        viewModel!!.fetchBlogs().observe(this) {
            postsList.clear()
            postsList.addAll(it.posts)
            adapter!!.notifyDataSetChanged()
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

    override fun onUserClicked(position: Int) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(postsList[position].URL)))
    }
}