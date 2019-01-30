package org.metabrainz.mobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.Release;
import org.metabrainz.mobile.fragment.contracts.EntityTab;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.util.CoverArt;
import org.metabrainz.mobile.util.CoverArt.Size;

public class CoverArtFragment extends Fragment implements EntityTab<Release> {

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
        ImageTag tag = tagFactory.build(url, getActivity());
        view.setTag(tag);
        App.getImageLoader().load(view);
    }

}
