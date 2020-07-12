package org.metabrainz.mobile.presentation.features.artist;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.databinding.ActivityArtistBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.links.LinksViewModel;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;

import java.util.Objects;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugArtistInfo";

    private ActivityArtistBinding binding;

    private ArtistViewModel artistViewModel;
    private ReleaseListViewModel releaseListViewModel;
    private LinksViewModel linksViewModel;
    private UserViewModel userViewModel;

    private ArtistPagerAdapter pagerAdapter;

    private String mbid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        artistViewModel = new ViewModelProvider(this).get(ArtistViewModel.class);
        releaseListViewModel = new ViewModelProvider(this).get(ReleaseListViewModel.class);
        linksViewModel = new ViewModelProvider(this).get(LinksViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) artistViewModel.setMBID(mbid);

        pagerAdapter = new ArtistPagerAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(pagerAdapter);
        binding.tabs.setupWithViewPager(binding.pager);

        /*
         * Whenever the artist changes, redraw the information
         * Subscribe to the empty live data and then ask the view model to update artist live data.
         * The approach has the benefit of eliminating extra calls to update artist info when it is
         * not required.
         * Example: The view model is shared between the activity and fragments. The activity and each
         * of the fragments will need to subscribe to the live data independently. In the earlier
         * approach, the artist data was queried whenever fetchData() was invoked, the repository
         * performed an update on the artist data. This approach led a lot of unneeded network request.
         * A better solution which is currently followed is that there is a separate method to subscribe
         * to live data and another one to update the artist info. The getData method acts
         * like a getter method.
         */
        artistViewModel.getData().observe(this, this::setArtist);
    }

    private void setArtist(Artist artist) {
        if (artist != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(artist.getName());
            userViewModel.setUserData(artist);
            releaseListViewModel.setData(artist.getReleases());
            linksViewModel.setData(artist.getRelations());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "artist/" + mbid);
    }
}
