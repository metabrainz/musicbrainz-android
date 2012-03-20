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

package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.LoginFragment.LoginCallbacks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class LoginActivity extends MusicBrainzActivity implements LoginCallbacks {

    public static final int DIALOG_PROGRESS = 0;
    private static final int DIALOG_LOGIN_FAILURE = 1;
    private static final int DIALOG_CONNECTION_FAILURE = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_PROGRESS:
            return createProgressDialog();
        case DIALOG_LOGIN_FAILURE:
            return createLoginFailureDialog();
        case DIALOG_CONNECTION_FAILURE:
            return createConnectionErrorDialog();
        }
        return null;
    }
    
    public Dialog createProgressDialog() {
        ProgressDialog progress = new ProgressDialog(this) {
            public void cancel() {
                super.cancel();
                getSupportLoaderManager().destroyLoader(0);
            }
        };
        progress.setMessage(getString(R.string.pd_authenticating));
        progress.setCancelable(true);
        return progress;
    }
    
    private Dialog createLoginFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(getText(R.string.auth_fail));
        builder.setCancelable(false);
        builder.setPositiveButton(getText(R.string.auth_pos), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    private Dialog createConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                //startLogin();
            }
        });
        builder.setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                LoginActivity.this.finish();
            }
        });
        return builder.create();
    }

    @Override
    public void onLoggedIn() {
        finish();
    }

}