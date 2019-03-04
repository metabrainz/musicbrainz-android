package org.metabrainz.mobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.search.entity.Artist;
import org.metabrainz.mobile.api.data.search.entity.Link;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.viewmodel.ArtistViewModel;

public class ArtistBioFragment extends Fragment {

    private ArtistViewModel artistViewModel;

    private TextView wikiTextView;
    private TextView artistType, artistGender, artistArea, artistLifeSpan;
    private Artist artist;

    public static ArtistBioFragment newInstance(Artist artist) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentFactory.Extra.ARTIST, artist);
        ArtistBioFragment fragment = new ArtistBioFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        artist = (Artist) activity.getIntent().getSerializableExtra(IntentFactory.Extra.ARTIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        findViews(layout);

        artistViewModel = ViewModelProviders.of(this).get(ArtistViewModel.class);
        wikiTextView = layout.findViewById(R.id.wiki_summary);

        artist = (Artist) getArguments().getSerializable(IntentFactory.Extra.ARTIST);
        setArtistInfo();
        getArtistWiki();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void findViews(View layout) {
        artistType = layout.findViewById(R.id.artist_type);
        artistGender = layout.findViewById(R.id.artist_gender);
        artistArea = layout.findViewById(R.id.artist_area);
        artistLifeSpan = layout.findViewById(R.id.life_span);
    }

    private void getArtistWiki(){
        String title = "";
        for(Link link: artist.getRelations()){
            if(link.getType().equals("wikipedia")) {
                title = link.getPageTitle();
                break;
            }
            if (link.getType().equals("wikidata")){
                title = link.getPageTitle();
                //TODO:Get Wiki url from wiki data id
                break;
            }
        }
        if (!title.isEmpty())
            artistViewModel.getArtistWiki(title).observe(this,
                wiki -> wikiTextView.setText(wiki.getExtract()));

    }

    private void setArtistInfo(){
        String type,gender,area,lifeSpan;

        type = artist.getType();
        gender = artist.getGender();
        area = artist.getArea().getName();
        if(artist.getLifeSpan() != null)
            lifeSpan = artist.getLifeSpan().getTimePeriod();
        else lifeSpan = "";
        if(type != null && !type.isEmpty())
            artistType.setText(type);
        if(gender != null && !gender.isEmpty())
            artistGender.setText(gender);
        if(area != null && !area.isEmpty())
            artistArea.setText(area);
        if(lifeSpan != null && !lifeSpan.isEmpty())
            artistLifeSpan.setText(lifeSpan);
    }
    /*@Override
    public void update(Artist artist) {
        setTimeSpan(artist);

        String wikiPage = getWikipediaPageName(artist);
        Bundle bioArgs = new Bundle();
        bioArgs.putString("mbid", mbid);
        if (!TextUtils.isEmpty(wikiPage)) {
            bioArgs.putString("wikiPage", wikiPage);
        }
        //getLoaderManager().initLoader(BIO_LOADER, bioArgs, this);
    }

    public void setTimeSpan(Artist artist) {
        String years = generateTimeSpan(artist);
        if (years.length() > 3) {
            yearsActive.setVisibility(View.VISIBLE);
            yearsActive.setText(years);
        }
    }

    public String getWikipediaPageName(Artist artist) {
        for (WebLink link : artist.getLinks()) {
            if (link.getUrl().contains("en.wikipedia")) {
                int pageSplit = link.getUrl().lastIndexOf("/") + 1;
                return link.getUrl().substring(pageSplit);
            }
        }
        return null;
    }

    public String generateTimeSpan(Artist artist) {
        StringBuilder years = new StringBuilder();
        if (!TextUtils.isEmpty(artist.getStart())) {
            years.append(artist.getStart());
        }
        years.append(" \u2013 ");
        if (!TextUtils.isEmpty(artist.getEnd())) {
            years.append(artist.getEnd());
        }
        return years.toString();
    }

   @Override
    public Loader<ArtistBio> onCreateLoader(int id, Bundle args) {
        return new ArtistBioLoader(args.getString("mbid"), args.getString("wikiPage"));
    }

    @Override
    public void onLoadFinished(Loader<ArtistBio> loader, ArtistBio data) {
        if (data == null) {
            bioText.setText(getString(R.string.bio_connection_fail));
        } else {
            setBioImage(data.getLastFmImage());
            if (!TextUtils.isEmpty(data.getWikipediaBio())) {
                showWikipediaBio(data);
            }
            getView().findViewById(R.id.loading).setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArtistBio> loader) {
        loader.reset();
    }

    public void setBioImage(String url) {
        Picasso.get().load(Uri.parse(url)).into(bioPicture);
    }

    public void showWikipediaBio(ArtistBio data) {
        bioText.setText(Html.fromHtml(data.getWikipediaBio()));
        showWikipediaCredit();
    }

    public void showWikipediaCredit() {
        //getView().findViewById(R.id.source_wikipedia).setVisibility(View.VISIBLE);
    }
    */
}
