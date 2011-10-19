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

package org.musicbrainz.mobile.task;

import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.activity.LoginActivity;

public class LoginTask extends IgnitedAsyncTask<LoginActivity, Credentials, Void, Boolean> {
    
    private boolean loginSuccess = false;

    @Override
    protected void onStart(LoginActivity activity) {
        activity.showDialog(LoginActivity.DIALOG_PROGRESS);
    }

    @Override
    protected Boolean run(Credentials... params) throws Exception {
        WebClient client = new WebClient(params[0]);
        return client.autenticateUserCredentials();
    }

    @Override
    protected void onSuccess(LoginActivity activity, Boolean loginResult) {
        loginSuccess = loginResult;
        completed(activity);
    }

    @Override
    protected void onError(LoginActivity activity, Exception e) {
        completed(activity);
    }
    
    private void completed(LoginActivity activity) {
        if (activity != null) {
            activity.dismissDialog(LoginActivity.DIALOG_PROGRESS);
            activity.onLoginTaskFinished();
        }
    }
    
    public boolean succeeded() {
        return loginSuccess;
    }
    
}
