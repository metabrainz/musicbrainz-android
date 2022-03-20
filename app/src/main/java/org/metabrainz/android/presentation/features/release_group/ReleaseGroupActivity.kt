package org.metabrainz.android.presentation.features.release_group

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.App
import org.metabrainz.android.data.sources.Constants
import org.metabrainz.android.data.sources.api.entities.mbentity.ReleaseGroup
import org.metabrainz.android.presentation.features.base.LookupActivity
import org.metabrainz.android.presentation.features.base.MusicBrainzFragment
import org.metabrainz.android.presentation.features.links.LinksFragment
import org.metabrainz.android.presentation.features.links.LinksViewModel
import org.metabrainz.android.presentation.features.release_list.ReleaseListFragment
import org.metabrainz.android.presentation.features.release_list.ReleaseListViewModel
import org.metabrainz.android.presentation.features.userdata.UserDataFragment
import org.metabrainz.android.presentation.features.userdata.UserViewModel


@AndroidEntryPoint
class ReleaseGroupActivity : LookupActivity<ReleaseGroup>() {

    private val releaseGroupViewModel: ReleaseGroupViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }
    private val linksViewModel: LinksViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }
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