package org.musicbrainz.mobile.fragment;

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.contracts.EntityTab;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.util.CoverArt;
import org.musicbrainz.mobile.util.CoverArt.Size;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;

public class CoverArtFragment extends Fragment implements EntityTab<Release>{

    private ImageTagFactory tagFactory;
    private String mbid;

    public static CoverArtFragment newInstance() {
        return new CoverArtFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mbid = activity.getIntent().getStringExtra(IntentFactory.Extra.RELEASE_MBID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tagFactory = ImageTagFactory.getInstance(App.getContext(), R.color.transparent);
        return inflater.inflate(R.layout.fragment_cover_art, container, false);
    }
    
    @Override
    public void update(Release entity) {
        setCover(R.id.cover_front, CoverArt.getFront(mbid, Size.LARGE));
        setCover(R.id.cover_back, CoverArt.getBack(mbid, Size.LARGE));
    }

    private void setCover(int imageViewId, String url) {
        ImageView view = (ImageView) getView().findViewById(imageViewId);
        ImageTag tag = tagFactory.build(url);
        view.setTag(tag);
        App.getImageLoader().load(view);
    }

}
