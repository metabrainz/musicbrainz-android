package org.metabrainz.mobile.presentation.features.release

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.App
import org.metabrainz.mobile.R
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.databinding.ActivityReleaseBinding
import org.metabrainz.mobile.presentation.MusicBrainzActivity
import org.metabrainz.mobile.presentation.features.fragments.ViewPagerAdapter
import org.metabrainz.mobile.presentation.features.links.LinksFragment
import org.metabrainz.mobile.presentation.features.links.LinksViewModel
import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel
import org.metabrainz.mobile.util.Resource

@AndroidEntryPoint
class ReleaseActivity : MusicBrainzActivity() {

    private lateinit var binding: ActivityReleaseBinding

    private val releaseViewModel: ReleaseViewModel by viewModels()
    private val linksViewModel: LinksViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var pagerAdapter: ViewPagerAdapter
    private val titles = listOf(R.string.tab_info, R.string.tab_releases,
            R.string.tab_links, R.string.tab_edits)

    private var mbid: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReleaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar(binding)

        mbid = intent.getStringExtra(Constants.MBID)
        if (mbid != null && mbid!!.isNotEmpty()) releaseViewModel.setMBID(mbid)

        pagerAdapter = ViewPagerAdapter(this,
                listOf(ReleaseInfoFragment, ReleaseTracksFragment, LinksFragment, UserDataFragment))
        binding.pager.adapter = pagerAdapter
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = resources.getText(titles[position])
        }.attach()

        binding.noResult.root.visibility = View.GONE
        binding.progressSpinner.root.visibility = View.VISIBLE
        binding.tabs.visibility = View.GONE
        binding.pager.visibility = View.GONE

        releaseViewModel.data.observe(this) { setRelease(it) }
    }

    private fun setRelease(resource: Resource<Release>) {
        binding.progressSpinner.root.visibility = View.GONE
        if (resource.status == Resource.Status.SUCCESS) {
            binding.noResult.root.visibility = View.GONE
            binding.tabs.visibility = View.VISIBLE
            binding.pager.visibility = View.VISIBLE

            val release = resource.data
            supportActionBar?.title = release.title
            userViewModel.setUserData(release)
            linksViewModel.setData(release.relations)
        } else binding.noResult.root.visibility = View.VISIBLE
    }

    override fun getBrowserURI(): Uri {
        if (mbid == null)
            return Uri.EMPTY
        return Uri.parse(App.WEBSITE_BASE_URL + "release/" + mbid)
    }

}