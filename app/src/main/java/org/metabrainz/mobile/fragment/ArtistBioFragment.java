package org.metabrainz.mobile.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

import com.squareup.picasso.Picasso;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.Artist;
import org.metabrainz.mobile.api.data.WebLink;
import org.metabrainz.mobile.async.external.ArtistBioLoader;
import org.metabrainz.mobile.async.external.result.ArtistBio;
import org.metabrainz.mobile.fragment.contracts.EntityTab;
import org.metabrainz.mobile.intent.IntentFactory;

public class ArtistBioFragment extends Fragment implements LoaderCallbacks<ArtistBio>, EntityTab<Artist> {

    private static final int BIO_LOADER = 30;

    private ImageView bioPicture;
    private TextView yearsActive;
    private TextView bioText;

    private String mbid;

    public static ArtistBioFragment newInstance() {
        return new ArtistBioFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mbid = activity.getIntent().getStringExtra(IntentFactory.Extra.ARTIST_MBID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bio, container, false);
        findViews(layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void findViews(View layout) {
        bioPicture = (ImageView) layout.findViewById(R.id.bio_picture);
        yearsActive = (TextView) layout.findViewById(R.id.years_active);
        bioText = (TextView) layout.findViewById(R.id.bio_text);
    }

    @Override
    public void update(Artist artist) {
        setTimeSpan(artist);

        String wikiPage = getWikipediaPageName(artist);
        Bundle bioArgs = new Bundle();
        bioArgs.putString("mbid", mbid);
        if (!TextUtils.isEmpty(wikiPage)) {
            bioArgs.putString("wikiPage", wikiPage);
        }
        getLoaderManager().initLoader(BIO_LOADER, bioArgs, this);
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
            } else {
                showLastFmBio(data);
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

    public void showLastFmBio(ArtistBio data) {
        if (TextUtils.isEmpty(data.getLastFmBio())) {
            bioText.setText(getString(R.string.bio_empty));
        } else {
            bioText.setText(Html.fromHtml(data.getLastFmBio()));
            showLastFmCredit();
        }
    }

    public void showWikipediaCredit() {
        getView().findViewById(R.id.source_wikipedia).setVisibility(View.VISIBLE);
    }

    public void showLastFmCredit() {
        getView().findViewById(R.id.source_lastfm).setVisibility(View.VISIBLE);
    }

}
