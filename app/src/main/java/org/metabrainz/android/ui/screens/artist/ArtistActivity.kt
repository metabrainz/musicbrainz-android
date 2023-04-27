package org.metabrainz.android.ui.screens.artist

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.android.application.App
import org.metabrainz.android.util.Constants
import org.metabrainz.android.model.mbentity.Artist
import org.metabrainz.android.ui.screens.links.LinksFragment
import org.metabrainz.android.ui.screens.links.LinksViewModel
import org.metabrainz.android.ui.screens.release_list.ReleaseListFragment
import org.metabrainz.android.ui.screens.release_list.ReleaseListViewModel
import org.metabrainz.android.ui.screens.userdata.UserDataFragment
import org.metabrainz.android.ui.screens.userdata.UserViewModel
import org.metabrainz.android.ui.screens.base.LookupActivity

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
@AndroidEntryPoint
class ArtistActivity : LookupActivity<Artist>() {

    private val artistViewModel: ArtistViewModel by viewModels()
    private val releaseListViewModel: ReleaseListViewModel by viewModels()
    private val linksViewModel: LinksViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }
    private val userViewModel: UserViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mbid = intent.getStringExtra(Constants.MBID)
        if (mbid != null && mbid.isNotEmpty())
            artistViewModel.mbid.value = mbid
        artistViewModel.data.observe(this) { processData(it) }
    }

    override fun getBrowserURI(): Uri {
        val mbid = artistViewModel.mbid.value ?: return Uri.EMPTY
        return Uri.parse(App.WEBSITE_BASE_URL + "artist/" + mbid)
    }

    override fun setData(data: Artist) {
        supportActionBar?.title = data.name
        userViewModel.setUserData(data)
        releaseListViewModel.setReleases(data.releases)
        linksViewModel.setData(data.relations)
    }

    override fun getFragmentsList() = listOf(ArtistBioFragment,
            ReleaseListFragment, LinksFragment, UserDataFragment
    )
}