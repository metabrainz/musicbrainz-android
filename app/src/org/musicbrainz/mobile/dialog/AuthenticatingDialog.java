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

package org.musicbrainz.mobile.dialog;

import org.musicbrainz.mobile.fragment.LoginFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AuthenticatingDialog extends DialogFragment {
    
    public static final String TAG = "loading";
    private static final String MESSAGE = "messageId";
    
    private Context context;

    public static AuthenticatingDialog newInstance(int messageId) {
        AuthenticatingDialog dialog = new AuthenticatingDialog();
        Bundle args = new Bundle();
        args.putInt(MESSAGE, messageId);
        dialog.setArguments(args);
        return dialog;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getMessage());
        dialog.setCancelable(true);
        return dialog;
    }
    
    private String getMessage() {
        int messageId = getArguments().getInt(MESSAGE);
        return context.getResources().getString(messageId);
    }
    
    @Override
    public void onCancel(DialogInterface dialog) {
        getLoaderManager().destroyLoader(LoginFragment.LOGIN_LOADER);
    }

}
