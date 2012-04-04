/*
 * Copyright (C) 2010 Jamie McDonald
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

package org.musicbrainz.mobile.dialog;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.BarcodeSearchActivity;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.string.StringMapper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Dialog that confirms the release details for barcode submission.
 */
public class BarcodeConfirmDialog extends Dialog implements View.OnClickListener {

    private BarcodeSearchActivity parent;
    private ReleaseStub stub;

    public BarcodeConfirmDialog(Context context, ReleaseStub stub) {
        super(context);

        this.stub = stub;
        parent = (BarcodeSearchActivity) context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bar_submit);

        Button confirm = (Button) findViewById(R.id.barcode_confirm);
        confirm.setOnClickListener(this);

        ((TextView) findViewById(R.id.list_release_title)).setText(stub.getTitle());
        ((TextView) findViewById(R.id.list_release_artist)).setText(StringFormat.commaSeparateArtists(stub.getArtists()));

        ((TextView) findViewById(R.id.list_release_tracksnum)).setText(stub.getTracksNum() + " " + parent.getString(R.string.label_tracks));
        ((TextView) findViewById(R.id.list_release_formats)).setText(StringMapper.buildReleaseFormatsString(
                getContext(), stub.getFormats()));

        ((TextView) findViewById(R.id.list_release_labels)).setText(StringFormat.commaSeparate(stub.getLabels()));
        ((TextView) findViewById(R.id.list_release_date)).setText(stub.getDate());
        ((TextView) findViewById(R.id.list_release_country)).setText(stub.getCountryCode());

        findViewById(R.id.release_box).setBackgroundResource(R.color.list_bg);
    }

    public void onClick(View v) {
        String releaseID = stub.getReleaseMbid();
        parent.submitBarcode(releaseID);
        dismiss();
    }

}
