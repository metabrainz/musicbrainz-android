package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.intent.zxing.IntentIntegrator;
import org.musicbrainz.mobile.view.DashTileView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class DashButtonsFragment extends ContextFragment implements OnClickListener {

    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_dash_buttons, container);
        setupTile(R.id.dash_scan, R.drawable.dash_scan, R.string.dash_scan);
        setupTile(R.id.dash_collections, R.drawable.dash_collections, R.string.dash_collections);
        setupTile(R.id.dash_donate, R.drawable.dash_donate, R.string.dash_donate);
        setupTile(R.id.dash_about, R.drawable.dash_about, R.string.dash_about);
        return layout;
    }

    private void setupTile(int tileId, int iconId, int stringId) {
        DashTileView scanTile = (DashTileView) layout.findViewById(tileId);
        scanTile.setIcon(iconId);
        scanTile.setText(stringId);
        scanTile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dash_scan:
            IntentIntegrator.initiateScan(getActivity(), getString(R.string.zx_title), getString(R.string.zx_message),
                    getString(R.string.zx_pos), getString(R.string.zx_neg), IntentIntegrator.PRODUCT_CODE_TYPES);
            break;
        case R.id.dash_collections:
            startActivity(IntentFactory.getCollectionList(context));
            break;
        case R.id.dash_donate:
            startActivity(IntentFactory.getDonate(context));
            break;
        case R.id.dash_about:
            startActivity(IntentFactory.getAbout(context));
        }
    }

}
