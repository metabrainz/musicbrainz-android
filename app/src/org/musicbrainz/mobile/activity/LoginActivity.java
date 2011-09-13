/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.activity;

import java.io.IOException;

import org.musicbrainz.android.api.ws.UserService;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.util.Secrets;
import org.musicbrainz.mobile.util.SimpleEncrypt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

/**
 * Activity to facilitate user authentication.
 * 
 * It would be nice not to store the password in user preferences. This could be
 * visible on a rooted device. Ideally we would just store
 * MD5(username:realm:password) as required for digest authentication.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class LoginActivity extends SuperActivity implements OnEditorActionListener {
	
	private EditText uname;
	private EditText pass;
	
	private CheckBox stayLogged;
	
	public static int NOT_LOGGED_IN = 0;
	public static int LOGGED_IN = 1;
	
	private String username;
	private String password;
	private boolean persist;
	
	private InputMethodManager imm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        setupActionBarWithHome();
        
        uname = (EditText) findViewById(R.id.uname_input);
        pass = (EditText) findViewById(R.id.pass_input);
        pass.setOnEditorActionListener(this);
        stayLogged = (CheckBox) findViewById(R.id.staylog_check);
        
        setResult(NOT_LOGGED_IN);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    
    public class AuthenticationTask extends AsyncTask<Void, Void, Integer> {
    	
    	private static final int SUCCESS = 0;
    	private static final int FAILURE = 1;
    	private static final int ERROR = 2;
    	
    	private ProgressDialog pd;

    	protected void onPreExecute() {
			pd = new ProgressDialog(LoginActivity.this) {
				public void cancel() {
					super.cancel();
					AuthenticationTask.this.cancel(true);
				}
			};
			pd.setMessage(getString(R.string.pd_authenticating));
			pd.setCancelable(true);
			pd.show();
    	}
    	
		protected Integer doInBackground(Void... v) {
			
			UserService user = new UserService(username, password, getClientVersion());
			Boolean success = false;
			try {
				success = user.autenticateUserCredentials();
			} catch (IOException e) {
				// connection failure
				e.printStackTrace();
				return ERROR;
			}
			
			user.shutdownConnectionManager();

			if (success)
				// authentication success
				return SUCCESS;
			else
				// authentication failure
				return FAILURE;
		}
		
		protected void onPostExecute(Integer resultCode) {
			
			switch (resultCode) {
			case SUCCESS:
				storeCredentials();
				
				setResult(LOGGED_IN);
				pd.dismiss();
				finish();
				break;
			case FAILURE:
				// authentication fail dialog
				AlertDialog.Builder failBuilder = new AlertDialog.Builder(LoginActivity.this);
				failBuilder.setMessage(getText(R.string.auth_fail))
				       .setCancelable(false)
				       .setPositiveButton(getText(R.string.auth_pos), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				
				try {
					AlertDialog authFail = failBuilder.create();
					pd.dismiss();
					authFail.show();
				} catch (Exception e) {
					Log.e("Login failed but Activity has closed anyway");
				}
				break;
			case ERROR:
				// connection fail dialog with option to restart auth thread
				AlertDialog.Builder errBuilder = new AlertDialog.Builder(LoginActivity.this);
				errBuilder.setMessage(getString(R.string.err_text))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.err_pos),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// restart search thread
								        new AuthenticationTask().execute();
										dialog.cancel();
									}
								})
						.setNegativeButton(getString(R.string.err_neg),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// finish activity
										LoginActivity.this.finish();
									}
								});
				try {
					Dialog conError = errBuilder.create();
					pd.dismiss();
					conError.show();
				} catch (Exception e) {
					Log.e("Connection timed out but Activity has closed anyway");
				}
			}
		}
    	
    }
    
    private void storeCredentials() {
    	Editor spe = getSharedPreferences("user", MODE_PRIVATE).edit();
		spe.putString("username", username);
		String obscuredPassword = SimpleEncrypt.encrypt(new Secrets().getKey(), password);
		spe.putString("password", obscuredPassword);
		spe.putBoolean("persist", persist);
		spe.commit();
    }

	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.auth_btn:
			login();
			break;
		case R.id.register_link:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.REGISTER_LINK)));
			break;
		case R.id.forgotpass_link:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.FORGOTPASS_LINK)));
		}
	}
	
	/*
	 * Initiate login on enter editor action.
	 */
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if (v.getId() == R.id.pass_input && actionId == EditorInfo.IME_NULL) 
			login();
		
		return false;
	}
	
	/*
	 * Start authentication task given the current username and password field text.
	 */
	private void login() {
		
		username = uname.getText().toString();
		password = pass.getText().toString();
		
		if (username.length() == 0 || password.length() == 0) {
			Toast warning = Toast.makeText(this, R.string.toast_auth_err, Toast.LENGTH_SHORT);
			warning.show();
		} else {
			imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
			persist = stayLogged.isChecked();
			new AuthenticationTask().execute();
		}
	}
    
}