package org.metabrainz.mobile.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.pager.ArtistPagerAdapter;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        artistViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.ARTIST_MBID);
        if(mbid != null && !mbid.isEmpty()) artistViewModel.setMBID(mbid);

        pagerAdapter = new ArtistPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Whenever the artist changes, redraw the information
        artistViewModel.initializeArtistData().observe(this, this::setArtist);
        artistViewModel.getArtistData();
    }

    private void setArtist(Artist artist){
        if (artist != null) getSupportActionBar().setTitle(artist.getName());
    }

    /*
    protected void populateLayout() {
        TextView artistText = findViewById(R.id.artist_artist);

        artistText.setText(artist.getName());
        ratingBar.setRating(artist.getRating());
        tagView.setText(StringFormat.commaSeparateTags(artist.getTags(), this));

        artistText.setSelected(true);
        tagView.setSelected(true);

        updateFragments();
        loading.setVisibility(View.GONE);
    }

    @SuppressWarnings("unchecked")
    private void updateFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            ((EntityTab<Artist>) fm.findFragmentByTag(pagerAdapter.makeTag(i))).update(artist);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist, menu);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        actionProvider.setShareIntent(Utils.shareIntent(Configuration.ARTIST_SHARE + mbid));
        return true;
    }

    private void showConnectionErrorWarning() {
        error.setVisibility(View.VISIBLE);
        Button retry = error.findViewById(R.id.retry_button);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getSupportLoaderManager().restartLoader(ARTIST_LOADER, null, ArtistActivity.this);
            }
        });
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public void updateTags(List<Tag> tags) {
        artist.setTags(tags);
        tagView.setText(StringFormat.commaSeparateTags(tags, this));
        getSupportLoaderManager().destroyLoader(ARTIST_LOADER);
    }

    @Override
    public void updateRating(Float rating) {
        artist.setRating(rating);
        ratingBar.setRating(rating);
        getSupportLoaderManager().destroyLoader(ARTIST_LOADER);
    }
    */

}
