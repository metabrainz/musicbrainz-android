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

import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.config.Secrets;
import org.musicbrainz.mobile.dialog.AuthenticatingDialog;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.loader.LoginLoader;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.util.PreferenceUtils.Pref;
import org.musicbrainz.mobile.util.SimpleEncrypt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginFragment extends ContextFragment implements LoaderCallbacks<AsyncResult<Boolean>>,
        OnEditorActionListener, OnClickListener {

    public static final int LOGIN_LOADER = 0;
    private static final String STATE_FAILURE = "showing_login_failure";
    private static final String STATE_CONNECTION = "showing_connection_error";

    private View layout;
    private EditText usernameField;
    private EditText passwordField;
    private LoginCallback loginCallback;

    public interface LoginCallback {
        public void onLoggedIn();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginCallback = (LoginCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + LoginCallback.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_login, container, false);
        usernameField = (EditText) layout.findViewById(R.id.uname_input);
        passwordField = (EditText) layout.findViewById(R.id.pass_input);
        passwordField.setOnEditorActionListener(this);
        layout.findViewById(R.id.login_btn).setOnClickListener(this);
        layout.findViewById(R.id.forgotpass_link).setOnClickListener(this);
        layout.findViewById(R.id.register_link).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(STATE_FAILURE)) {
                layout.findViewById(R.id.login_failure_warning).setVisibility(View.VISIBLE);
            } else if (savedInstanceState.getBoolean(STATE_CONNECTION)) {
                layout.findViewById(R.id.login_connection_warning).setVisibility(View.VISIBLE);
            }
        }

        continueLogin();
    }

    private void continueLogin() {
        LoaderManager loaderManager = getLoaderManager();

        if (loaderManager.getLoader(LOGIN_LOADER) != null) {
            loaderManager.initLoader(LOGIN_LOADER, null, this);
        }
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
            startActivity(IntentFactory.getWebView(context, R.string.web_register, Configuration.URL_REGISTER));
            break;
        case R.id.forgotpass_link:
            startActivity(IntentFactory.getWebView(context, R.string.web_forgot_pass,
                    Configuration.URL_FORGOT_PASS));
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
        hideWarnings();
        showLoadingDialog();
        getLoaderManager().initLoader(LOGIN_LOADER, null, this);
    }

    private void showLoadingDialog() {
        DialogFragment loadingDialog = AuthenticatingDialog.newInstance(R.string.pd_authenticating);
        loadingDialog.show(getFragmentManager(), AuthenticatingDialog.TAG);
    }

    private void dismissLoadingDialog() {
        Fragment frag = getFragmentManager().findFragmentByTag(AuthenticatingDialog.TAG);
        if (frag != null) {
            AuthenticatingDialog loading = (AuthenticatingDialog) frag;
            loading.getDialog().dismiss();
        }
    }

    private void onLoginSuccess() {
        storeLoginData();
        MusicBrainzApp.updateLoginStatus(true);
        Toast.makeText(context, R.string.toast_logged_in, Toast.LENGTH_SHORT).show();
        loginCallback.onLoggedIn();
    }

    public void storeLoginData() {
        Editor spe = context.getSharedPreferences(Pref.PREFS_USER, Context.MODE_PRIVATE).edit();
        spe.putString(Pref.PREF_USERNAME, getUsername());
        String obscuredPassword = SimpleEncrypt.encrypt(new Secrets().getKey(), getPassword());
        spe.putString(Pref.PREF_PASSWORD, obscuredPassword);
        spe.commit();
    }

    public void hideWarnings() {
        hideLoginFailureWarning();
        hideConnectionErrorWarning();
    }

    private void showLoginFailureWarning() {
        layout.findViewById(R.id.login_failure_warning).setVisibility(View.VISIBLE);
    }

    private void hideLoginFailureWarning() {
        layout.findViewById(R.id.login_failure_warning).setVisibility(View.GONE);
    }

    private void showConnectionErrorWarning() {
        layout.findViewById(R.id.login_connection_warning).setVisibility(View.VISIBLE);
    }

    private void hideConnectionErrorWarning() {
        layout.findViewById(R.id.login_connection_warning).setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_FAILURE,
                layout.findViewById(R.id.login_failure_warning).getVisibility() == View.VISIBLE);
        outState.putBoolean(STATE_CONNECTION,
                layout.findViewById(R.id.login_connection_warning).getVisibility() == View.VISIBLE);
    }

    @Override
    public Loader<AsyncResult<Boolean>> onCreateLoader(int id, Bundle args) {
        return new LoginLoader(context, getUsername(), getPassword());
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<Boolean>> loader, AsyncResult<Boolean> data) {
        getLoaderManager().destroyLoader(LOGIN_LOADER);
        dismissLoadingDialog();
        handleLoginResult(data);
    }

    private void handleLoginResult(AsyncResult<Boolean> result) {
        switch (result.getStatus()) {
        case SUCCESS:
            if (result.getData()) {
                onLoginSuccess();
            } else {
                showLoginFailureWarning();
            }
            break;
        case EXCEPTION:
            showConnectionErrorWarning();
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<Boolean>> loader) {
        loader.reset();
    }

}
