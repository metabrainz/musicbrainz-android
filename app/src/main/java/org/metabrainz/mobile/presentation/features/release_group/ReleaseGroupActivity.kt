package org.metabrainz.mobile.presentation.features.release_group

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.App
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup
import org.metabrainz.mobile.presentation.features.base.LookupActivity
import org.metabrainz.mobile.presentation.features.base.MusicBrainzFragment
import org.metabrainz.mobile.presentation.features.links.LinksFragment
import org.metabrainz.mobile.presentation.features.links.LinksViewModel
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListFragment
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel
import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel


@AndroidEntryPoint
class ReleaseGroupActivity : LookupActivity<ReleaseGroup>() {

    private val releaseGroupViewModel: ReleaseGroupViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val linksViewModel: LinksViewModel by viewModels()
    private val releaseListViewModel: ReleaseListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mbid = intent.getStringExtra(Constants.MBID)
        if (mbid != null && mbid.isNotEmpty())
            releaseGroupViewModel.mbid.value = mbid
        releaseGroupViewModel.data.observe(this) { processData(it) }
    }

    override fun setData(data: ReleaseGroup) {
        supportActionBar?.title = data.title

        userViewModel.setUserData(data)
        linksViewModel.setData(data.relations)
        releaseListViewModel.setReleases(data.releases)
    }

    override fun getFragmentsList(): List<MusicBrainzFragment> = listOf(ReleaseGroupInfoFragment,
            ReleaseListFragment, LinksFragment, UserDataFragment)

    override fun getBrowserURI(): Uri {
        val mbid = releaseGroupViewModel.mbid.value ?: return Uri.EMPTY
        return Uri.parse(App.WEBSITE_BASE_URL + "release-group/" + mbid)
    }

}