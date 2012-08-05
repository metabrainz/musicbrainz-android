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

import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BarcodeNotFoundDialog extends DialogFragment {

    public static final String TAG = "barcode_not_found";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.barcode_header);

        if (MusicBrainzApp.getApp(getActivity()).isUserLoggedIn()) {
            builder.setMessage(R.string.barcode_info_log);
        } else {
            builder.setMessage(R.string.barcode_info_nolog);
        }

        builder.setPositiveButton(R.string.barcode_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ((ReleaseActivity) getActivity()).doPositiveClick();
            }
        });
        return builder.create();
    }

}
