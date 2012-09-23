package org.musicbrainz.mobile.fragment;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.async.lastfm.ArtistBioLoader;
import org.musicbrainz.mobile.async.lastfm.Response.LastFmArtist;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.string.StringFormat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.imageloader.core.model.ImageTagFactory;

public class ArtistBioFragment extends Fragment implements LoaderCallbacks<LastFmArtist>{
    
    private static final int BIO_LOADER = 30;

    private ImageTagFactory tagFactory;
    
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
        tagFactory = new ImageTagFactory(App.getContext(), R.color.transparent);
        getLoaderManager().initLoader(BIO_LOADER, null, this);
    }

    private void findViews(View layout) {
        bioPicture = (ImageView) layout.findViewById(R.id.bio_picture);
        yearsActive = (TextView) layout.findViewById(R.id.years_active);
        bioText = (TextView) layout.findViewById(R.id.bio_text);
    }
    
    public void update(Artist artist) {
        String years = generateTimeSpan(artist);
        if (years.length() > 3) {
            yearsActive.setVisibility(View.VISIBLE);
            yearsActive.setText(years);
        }
    }

    public String generateTimeSpan(Artist artist) {
        StringBuilder years = new StringBuilder();
        if (!TextUtils.isEmpty(artist.getStart())) {
            years.append(artist.getStart());
        }
        years.append(" Ñ ");
        if (!TextUtils.isEmpty(artist.getEnd())) {
            years.append(artist.getEnd());
        }
        return years.toString();
    }

    @Override
    public Loader<LastFmArtist> onCreateLoader(int id, Bundle args) {
        return new ArtistBioLoader(mbid);
    }

    @Override
    public void onLoadFinished(Loader<LastFmArtist> loader, LastFmArtist data) {
        if (data == null) {
            bioText.setText(getString(R.string.bio_connection_fail));
        } else {
            setBioImage(data.image.get(4).text);
            String bio = StringFormat.lineBreaksToHtml(data.bio.full);
            setBioText(TextUtils.isEmpty(bio) ? getString(R.string.bio_empty) : bio);
        }
    }

    public void setBioImage(String url) {
        bioPicture.setTag(tagFactory.build(url));
        App.getImageLoader().load(bioPicture);
    }

    public void setBioText(String bio) {
        bio = StringFormat.stripFromEnd("<br/>User-contributed", bio);
        bioText.setText(Html.fromHtml(bio));
        bioText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    @Override
    public void onLoaderReset(Loader<LastFmArtist> loader) {
        loader.reset();
    }

}
