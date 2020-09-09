package org.metabrainz.mobile.presentation.features.release

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.App
import org.metabrainz.mobile.R
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.presentation.features.base.LookupActivity
import org.metabrainz.mobile.presentation.features.base.MusicBrainzFragment
import org.metabrainz.mobile.presentation.features.links.LinksFragment
import org.metabrainz.mobile.presentation.features.links.LinksViewModel
import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel

@AndroidEntryPoint
class ReleaseActivity : LookupActivity<Release>() {

    private val releaseViewModel: ReleaseViewModel by viewModels()
    private val linksViewModel: LinksViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mbid = intent.getStringExtra(Constants.MBID)
        if (mbid != null && mbid.isNotEmpty())
            releaseViewModel.mbid.value = mbid
        releaseViewModel.data.observe(this) { processData(it) }
    }

    override fun setData(data: Release) {
        supportActionBar?.title = data.title
        userViewModel.setUserData(data)
        linksViewModel.setData(data.relations)
    }

    override fun getTabsList(): List<Int> = listOf(R.string.tab_info,
            R.string.tab_mediums, R.string.tab_links, R.string.tab_edits)

    override fun getFragmentsList(): List<MusicBrainzFragment> = listOf(ReleaseInfoFragment,
            ReleaseTracksFragment, LinksFragment, UserDataFragment)

    override fun getBrowserURI(): Uri {
        val mbid = releaseViewModel.mbid.value ?: return Uri.EMPTY
        return Uri.parse(App.WEBSITE_BASE_URL + "release/" + mbid)
    }

}