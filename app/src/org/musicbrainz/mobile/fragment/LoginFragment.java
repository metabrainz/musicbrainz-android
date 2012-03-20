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
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.config.Constants;
import org.musicbrainz.mobile.config.Secrets;
import org.musicbrainz.mobile.loader.LoginLoader;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.util.SimpleEncrypt;
import org.musicbrainz.mobile.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class LoginFragment extends ContextFragment implements LoaderCallbacks<AsyncResult<Boolean>>,
        OnEditorActionListener, OnClickListener {
    
    private static final int LOGIN_LOADER = 0;

    private View layout;
    private EditText usernameField;
    private EditText passwordField;
    private LoginCallbacks loginCallbacks;

    public interface LoginCallbacks {
        public void onLoggedIn();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginCallbacks = (LoginCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + LoginCallbacks.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_login, container);
        findViews();
        return layout;
    }

    private void findViews() {
        usernameField = (EditText) layout.findViewById(R.id.uname_input);
        passwordField = (EditText) layout.findViewById(R.id.pass_input);
        passwordField.setOnEditorActionListener(this);
        layout.findViewById(R.id.login_btn).setOnClickListener(this);
        layout.findViewById(R.id.forgotpass_link).setOnClickListener(this);
        layout.findViewById(R.id.register_link).setOnClickListener(this);
    }
    
    private String getUsername() {
        return usernameField.getText().toString();
    }

    private String getPassword() {
        return passwordField.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_btn:
            tryLogin();
            break;
        case R.id.register_link:
            startActivity(Utils.urlIntent(Configuration.REGISTER_LINK));
            break;
        case R.id.forgotpass_link:
            startActivity(Utils.urlIntent(Configuration.FORGOTPASS_LINK));
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.pass_input && actionId == EditorInfo.IME_NULL) {
            tryLogin();
        }
        return false;
    }

    private void tryLogin() {
        if (getUsername().length() == 0 || getPassword().length() == 0) {
            Toast.makeText(context, R.string.toast_auth_err, Toast.LENGTH_SHORT).show();
        } else {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
            startLogin();
        }
    }

    private void startLogin() {
        // TODO show loading dialog
        getLoaderManager().initLoader(LOGIN_LOADER, null, this);
    }

    private void onLoginSuccess() {
        Editor spe = context.getSharedPreferences(Constants.PREFS_USER, Context.MODE_PRIVATE).edit();
        spe.putString(Constants.PREF_USERNAME, getUsername());
        String obscuredPassword = SimpleEncrypt.encrypt(new Secrets().getKey(), getPassword());
        spe.putString(Constants.PREF_PASSWORD, obscuredPassword);
        spe.commit();
        MusicBrainzApplication app = (MusicBrainzApplication) context;
        app.updateLoginStatus(true);
        Toast.makeText(context, R.string.toast_logged_in, Toast.LENGTH_SHORT).show();
        loginCallbacks.onLoggedIn();
    }

    @Override
    public Loader<AsyncResult<Boolean>> onCreateLoader(int id, Bundle args) {
        return new LoginLoader(context, getUsername(), getPassword());
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<Boolean>> loader, AsyncResult<Boolean> data) {
        getLoaderManager().destroyLoader(LOGIN_LOADER);
        // TODO dismiss loading dialog
        handleLoginResult(data);
    }
    
    private void handleLoginResult(AsyncResult<Boolean> result) {
        switch(result.getStatus()) {
        case SUCCESS:
            if (result.getData()) {
                onLoginSuccess();
            } else {
                // TODO show login fail
            }
            break;
        case EXCEPTION:
            // TODO show connection fail
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<Boolean>> loader) {
        loader.reset();
    }

}
