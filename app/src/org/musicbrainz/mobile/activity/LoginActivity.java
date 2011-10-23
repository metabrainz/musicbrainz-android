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

package org.musicbrainz.mobile.activity;

import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.task.LoginTask;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Constants;
import org.musicbrainz.mobile.util.Secrets;
import org.musicbrainz.mobile.util.SimpleEncrypt;
import org.musicbrainz.mobile.util.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends SuperActivity implements OnEditorActionListener {

    public static final int RESULT_NOT_LOGGED_IN = 0;
    public static final int RESULT_LOGGED_IN = 1;

    public static final int DIALOG_PROGRESS = 0;
    private static final int DIALOG_LOGIN_FAILURE = 1;
    private static final int DIALOG_CONNECTION_FAILURE = 2;

    private EditText usernameBox;
    private EditText passwordBox;

    private String username;
    private String password;

    private LoginTask loginTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setupActionBarWithHome();

        usernameBox = (EditText) findViewById(R.id.uname_input);
        passwordBox = (EditText) findViewById(R.id.pass_input);
        passwordBox.setOnEditorActionListener(this);

        setResult(RESULT_NOT_LOGGED_IN);

        Object retained = getLastNonConfigurationInstance();
        if (getLastNonConfigurationInstance() instanceof LoginTask) {
            loginTask = (LoginTask) retained;
            loginTask.connect(this);
        } else {
            setupLoginTask();
        }
    }

    private void setupLoginTask() {
        loginTask = new LoginTask();
        loginTask.connect(this);
    }

    private void executeLogin() {
        Credentials creds = new Credentials(getUserAgent(), username, password, getClientId());
        loginTask.execute(creds);
    }

    public void onLoginTaskFinished() {
        if (loginTask.failed()) {
            showDialog(DIALOG_CONNECTION_FAILURE);
        } else if (loginTask.succeeded()) {
            onLoginSuccess();
        } else {
            showDialog(DIALOG_LOGIN_FAILURE);
        }
    }

    private void onLoginSuccess() {
        Editor spe = getSharedPreferences(Constants.PREFS_USER, MODE_PRIVATE).edit();
        spe.putString(Constants.PREF_USERNAME, username);
        String obscuredPassword = SimpleEncrypt.encrypt(new Secrets().getKey(), password);
        spe.putString(Constants.PREF_PASSWORD, obscuredPassword);
        spe.commit();
        setResult(RESULT_LOGGED_IN);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.auth_btn:
            tryLogin();
            break;
        case R.id.register_link:
            startActivity(Utils.urlIntent(Config.REGISTER_LINK));
            break;
        case R.id.forgotpass_link:
            startActivity(Utils.urlIntent(Config.FORGOTPASS_LINK));
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.pass_input && actionId == EditorInfo.IME_NULL) {
            tryLogin();
        }
        return false;
    }

    private void tryLogin() {
        username = usernameBox.getText().toString();
        password = passwordBox.getText().toString();

        if (username.length() == 0 || password.length() == 0) {
            Toast.makeText(this, R.string.toast_auth_err, Toast.LENGTH_SHORT).show();
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(passwordBox.getWindowToken(), 0);
            executeLogin();
        }
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
                loginTask.cancel(true);
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
                setupLoginTask();
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
                setupLoginTask();
                executeLogin();
                dialog.cancel();
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
    public Object onRetainNonConfigurationInstance() {
        loginTask.disconnect();
        return loginTask;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginTask.disconnect();
    }

}