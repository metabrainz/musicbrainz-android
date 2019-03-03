package org.metabrainz.mobile.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.pager.ArtistPagerAdapter;
import org.metabrainz.mobile.api.data.Artist;
import org.metabrainz.mobile.api.data.Tag;
import org.metabrainz.mobile.api.data.UserData;
import org.metabrainz.mobile.async.ArtistLoader;
import org.metabrainz.mobile.async.result.AsyncEntityResult;
import org.metabrainz.mobile.config.Configuration;
import org.metabrainz.mobile.fragment.EditFragment;
import org.metabrainz.mobile.fragment.contracts.EntityTab;
import org.metabrainz.mobile.intent.IntentFactory.Extra;
import org.metabrainz.mobile.string.StringFormat;
import org.metabrainz.mobile.util.Utils;

import java.util.List;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends MusicBrainzActivity implements LoaderCallbacks<AsyncEntityResult<Artist>>,
        EditFragment.Callback {

    private static final int ARTIST_LOADER = 0;

    private String mbid;
    private Artist artist;
    private UserData userData;

    private View loading;
    private View error;

    private RatingBar ratingBar;
    private TextView tagView;

    private ArtistPagerAdapter pagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        mbid = getIntent().getStringExtra(Extra.ARTIST_MBID);
        setContentView(R.layout.activity_artist);
        configurePager();
        findViews();
        setSupportProgressBarIndeterminateVisibility(false);
        getSupportLoaderManager().initLoader(ARTIST_LOADER, savedInstanceState, this);
    }

    private void configurePager() {
        pagerAdapter = new ArtistPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        //TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        //indicator.setViewPager(pager);
        pager.setCurrentItem(1);
        pager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
    }

    private void findViews() {
        loading = findViewById(R.id.loading);
        error = findViewById(R.id.error);
        ratingBar = findViewById(R.id.rating);
        tagView = findViewById(R.id.tags);
    }

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

    @Override
    public Loader<AsyncEntityResult<Artist>> onCreateLoader(int id, Bundle args) {
        return new ArtistLoader(mbid);
    }

    @Override
    public void onLoadFinished(Loader<AsyncEntityResult<Artist>> loader, AsyncEntityResult<Artist> container) {
        handleLoadResult(container);
    }

    private void handleLoadResult(AsyncEntityResult<Artist> result) {
        switch (result.getStatus()) {
            case SUCCESS:
                artist = result.getData();
                userData = result.getUserData();
                populateLayout();
                break;
            case EXCEPTION:
                showConnectionErrorWarning();
        }
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
    public void onLoaderReset(Loader<AsyncEntityResult<Artist>> loader) {
        loader.reset();
    }

    @Override
    public void showLoading() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public String getMbid() {
        return mbid;
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

}
