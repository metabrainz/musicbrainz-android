/*
 * Copyright (C) 2012 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.MusicBrainzApplication;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.AboutActivity;
import org.musicbrainz.mobile.activity.CollectionListActivity;
import org.musicbrainz.mobile.activity.DonateActivity;
import org.musicbrainz.mobile.activity.LoginActivity;
import org.musicbrainz.mobile.intent.zxing.IntentIntegrator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class DashButtonsFragment extends ContextFragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dash_buttons, container);
        layout.findViewById(R.id.scan_btn).setOnClickListener(this);
        layout.findViewById(R.id.collection_btn).setOnClickListener(this);
        layout.findViewById(R.id.donate_btn).setOnClickListener(this);
        layout.findViewById(R.id.about_btn).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.scan_btn:
            IntentIntegrator.initiateScan(getActivity(), getString(R.string.zx_title), getString(R.string.zx_message),
                    getString(R.string.zx_pos), getString(R.string.zx_neg), IntentIntegrator.PRODUCT_CODE_TYPES);
            break;
        case R.id.collection_btn:
            MusicBrainzApplication app = (MusicBrainzApplication) context;
            if (app.isUserLoggedIn()) {
                startActivity(new Intent(context, CollectionListActivity.class));
            } else {
                startActivity(new Intent(context, LoginActivity.class));
            }
            break;
        case R.id.donate_btn:
            startActivity(new Intent(context, DonateActivity.class));
            break;
        case R.id.about_btn:
            startActivity(new Intent(context, AboutActivity.class));
        }
    }

}
