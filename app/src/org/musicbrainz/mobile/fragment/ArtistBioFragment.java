package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.async.lastfm.ArtistBioLoader;
import org.musicbrainz.mobile.async.lastfm.Response.Artist;
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

public class ArtistBioFragment extends Fragment implements LoaderCallbacks<Artist>{
    
    private static final int BIO_LOADER = 30;

    private ImageTagFactory tagFactory;
    
    private ImageView bioPicture;
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
        tagFactory = new ImageTagFactory(App.getContext(), R.drawable.progress_spinner);
        getLoaderManager().initLoader(BIO_LOADER, null, this);
    }

    private void findViews(View layout) {
        bioPicture = (ImageView) layout.findViewById(R.id.bio_picture);
        bioText = (TextView) layout.findViewById(R.id.bio_text);
    }

    @Override
    public Loader<Artist> onCreateLoader(int id, Bundle args) {
        return new ArtistBioLoader(mbid);
    }

    @Override
    public void onLoadFinished(Loader<Artist> loader, Artist data) {
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
    public void onLoaderReset(Loader<Artist> loader) {
        loader.reset();
    }

}
