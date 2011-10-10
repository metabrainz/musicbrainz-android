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

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.BarcodeSearchActivity;
import org.musicbrainz.mobile.activity.ReleaseActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Dialog to handle barcode scans which are not yet stored in MusicBrainz.
 */
public class BarcodeResultDialog extends Dialog implements View.OnClickListener {

    private ReleaseActivity parent;
    private String barcode;

    public BarcodeResultDialog(Context context, boolean loggedIn, String barcode) {
        super(context);

        parent = (ReleaseActivity) context;
        this.barcode = barcode;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_barcode);

        TextView text = (TextView) findViewById(R.id.barcode_dialog_text);

        Button add = (Button) findViewById(R.id.barcode_add);
        add.setOnClickListener(this);

        if (loggedIn) {
            text.setText(R.string.barcode_info_log);
        } else {
            text.setText(R.string.barcode_info_nolog);
            add.setEnabled(false);
        }
    }

    public void onClick(View v) {

        Intent barcodeIntent = new Intent(parent, BarcodeSearchActivity.class);
        barcodeIntent.putExtra("barcode", barcode);
        parent.startActivity(barcodeIntent);
        parent.finish();
        dismiss();
    }

    public void cancel() {
        super.cancel();
        parent.finish();
    }

    @Override
    public boolean onSearchRequested() {
        return false;
    }

}
