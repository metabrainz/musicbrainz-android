package org.metabrainz.mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.list.ArtistLinkAdapter;
import org.metabrainz.mobile.adapter.list.ArtistReleaseAdapter;
import org.metabrainz.mobile.api.data.ArtistWikiSummary;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Link;
import org.metabrainz.mobile.api.data.search.entity.Release;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.repository.LookupRepository;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
public class ArtistActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugArtistInfo";

    private ArtistViewModel artistViewModel;
    private RecyclerView releasesRecyclerView, linksRecyclerView;
    private ArtistReleaseAdapter releaseAdapter;
    private ArtistLinkAdapter linkAdapter;
    private TextView wikiTextView, artistType, artistGender, artistArea, artistLifeSpan;
    private View wikiCard;

    private String mbid;
    private List<Release> releaseList;
    private List<Link> linkList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();

        artistViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);
        releaseList = new ArrayList<>();
        linkList = new ArrayList<>();

        releaseAdapter = new ArtistReleaseAdapter(this, releaseList);
        releasesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        releasesRecyclerView.setAdapter(releaseAdapter);

        linkAdapter = new ArtistLinkAdapter(this,linkList);
        linksRecyclerView.setAdapter(linkAdapter);
        linksRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mbid = getIntent().getStringExtra(IntentFactory.Extra.ARTIST_MBID);
        if(mbid != null && !mbid.isEmpty()) artistViewModel.setMBID(mbid);

        // Whenever the artist changes, redraw the information
        artistViewModel.getArtistData().observe(this, this::setArtist);
    }

    private void findViews() {
        artistType = findViewById(R.id.artist_type);
        artistGender = findViewById(R.id.artist_gender);
        artistArea = findViewById(R.id.artist_area);
        artistLifeSpan = findViewById(R.id.life_span);
        wikiCard = findViewById(R.id.card_artist_wiki);
        wikiTextView = findViewById(R.id.wiki_summary);
        releasesRecyclerView = findViewById(R.id.recycler_view);
        linksRecyclerView = findViewById(R.id.links_list);
    }

    private void setArtist(Artist artist){
        if (artist != null){
            getSupportActionBar().setTitle(artist.getName());

            getArtistWiki(artist);

            String type = artist.getType();
            if (type != null && !type.isEmpty()) {
                artistType.setText(type);
            }

            String gender = artist.getGender();
            if (gender != null && !gender.isEmpty()) {
                artistGender.setText(gender);
            }

            if (artist.getArea() != null) {
                artistArea.setText(artist.getArea().getName());
            }

            if (artist.getLifeSpan() != null) {
                artistLifeSpan.setText(artist.getLifeSpan().getTimePeriod());
            }

            if (artist.getReleases() != null){
                // Maybe releases get updated with cover art info
                // Notify the releaseAdapter that the info is new
                releaseList.clear();
                releaseList.addAll(artist.getReleases());
                releaseAdapter.notifyDataSetChanged();
            }

            if(artist.getRelations() != null){
                linkList.clear();
                linkList.addAll(artist.getRelations());
                linkAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getArtistWiki(Artist artist){
        String title = "";
        int method = -1;
        if(artist != null)
        for(Link link: artist.getRelations()){
            if(link.getType().equals("wikipedia")) {
                title = link.getPageTitle();
                method = LookupRepository.METHOD_WIKIPEDIA_URL;
                break;
            }
            if (link.getType().equals("wikidata")){
                title = link.getPageTitle();
                method = LookupRepository.METHOD_WIKIDATA_ID;
                break;
            }
        }
        if (method != -1) artistViewModel.getArtistWiki(title, method)
                .observe(this, this::setWiki );
        else hideWikiCard();

    }

    private void setWiki(ArtistWikiSummary wiki){
        if (wiki != null){
            String wikiText = wiki.getExtract();
            if(wikiText != null && !wikiText.isEmpty()) {
                showWikiCard();
                wikiTextView.setText(wikiText);
            } else hideWikiCard();
        }else hideWikiCard();
    }

    private void showWikiCard(){
        wikiCard.setVisibility(View.VISIBLE);
    }
    private void hideWikiCard(){
        wikiCard.setVisibility(View.GONE);
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
