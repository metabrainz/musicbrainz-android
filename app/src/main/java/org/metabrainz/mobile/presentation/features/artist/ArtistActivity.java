package org.metabrainz.mobile.presentation.features.artist;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;

import java.util.Objects;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugArtistInfo";

    private ArtistViewModel artistViewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArtistPagerAdapter pagerAdapter;

    private String mbid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        artistViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.ARTIST_MBID);
        if (mbid != null && !mbid.isEmpty()) artistViewModel.setMBID(mbid);

        pagerAdapter = new ArtistPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        /*
         * Whenever the artist changes, redraw the information
         * Subscribe to the empty live data and then ask the view model to update artist live data.
         * The approach has the benefit of eliminating extra calls to update artist info when it is
         * not required.
         * Example: The view model is shared between the activity and fragments. The activity and each
         * of the fragments will need to subscribe to the live data independently. In the earlier
         * approach, the artist data was queried whenever getArtistData() was invoked, the repository
         * performed an update on the artist data. This approach led a lot of unneeded network request.
         * A better solution which is currently followed is that there is a separate method to subscribe
         * to live data and another one to update the artist info. The initializeLiveData method acts
         * like a getter method.
         */
        artistViewModel.initializeArtistData().observe(this, this::setArtist);
        artistViewModel.getArtistData();
    }

    private void setArtist(Artist artist) {
        if (artist != null)
            Objects.requireNonNull(getSupportActionBar()).setTitle(artist.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
