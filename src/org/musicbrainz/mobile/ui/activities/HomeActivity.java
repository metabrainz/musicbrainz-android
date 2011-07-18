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

package org.musicbrainz.mobile.ui.activities;

import org.musicbrainz.mobile.R;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Main Activity which allows the user to initiate any of the high level
 * tasks facilitated by the application.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class HomeActivity extends SuperActivity implements OnEditorActionListener {
	
	private EditText searchTerm;
	private Spinner searchType;
	private Button login;
	private InputMethodManager imm;
	
	private static final int LOGIN_REQUEST = 0; // authentication request code

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);

		searchTerm = (EditText) findViewById(R.id.query_input);
		searchTerm.setOnEditorActionListener(this);

		// create search type spinner from XML resource
		searchType = (Spinner) findViewById(R.id.search_spin);
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
				R.array.searchType, 
				android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchType.setAdapter(typeAdapter);

		login = (Button) findViewById(R.id.login_btn);
		
		// set label based on login state
		if (loggedIn)
			login.setText(R.string.logout_label);
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/*
	 * Handle result of barcode scan.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if (requestCode == LOGIN_REQUEST) {
			// get login result
			if (resultCode == AuthenticationActivity.LOGGED_IN) {
				loggedIn = true;
				login.setText(R.string.logout_label);
				Toast loginMessage = Toast.makeText(this, R.string.toast_loggedIn, Toast.LENGTH_SHORT);
				loginMessage.show();
			} 
		} else if (requestCode == IntentIntegrator.REQUEST_CODE) {
			
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);
			if (scanResult.getContents() != null) {

				Intent barcodeResult = new Intent(this, ReleaseActivity.class);
				barcodeResult.putExtra("barcode", scanResult.getContents());
				
				startActivity(barcodeResult);
			}
		}
	}

	/*
	 * Listener for button clicks.
	 */
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.login_btn:
			if (!loggedIn) {
				Intent logInIntent = new Intent(this, AuthenticationActivity.class);
				startActivityForResult(logInIntent, LOGIN_REQUEST);
			} else {
				SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
				Editor spe = prefs.edit();
				spe.clear();
				spe.commit();
				login.setText(R.string.login_label);
				loggedIn = false;
			}
			break;
		case R.id.scan_btn:
			// ZXing integration with external strings and product code mode
			IntentIntegrator.initiateScan(HomeActivity.this,
					getString(R.string.zx_title),
					getString(R.string.zx_message),
					getString(R.string.zx_pos), 
					getString(R.string.zx_neg),
					IntentIntegrator.PRODUCT_CODE_TYPES);
			break;
		case R.id.search_btn:
			startSearch();
			break;
		case R.id.about_btn:
			Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
			break;
		case R.id.donate_btn:
			Intent donateIntent = new Intent(this, DonateActivity.class);
			startActivity(donateIntent);
		}
	}

	/*
	 * Start a search activity based on the current values of search type and
	 * term.
	 */
	private void startSearch() {
		
		String term = searchTerm.getText().toString();

		if (term.length() != 0) {
			imm.hideSoftInputFromWindow(searchTerm.getWindowToken(), 0);
			int pos = searchType.getSelectedItemPosition();
			
			Intent searchIntent = new Intent(this, SearchResultsActivity.class);
			
			searchIntent.putExtra("term", term);
			searchIntent.putExtra("type", pos);

			startActivity(searchIntent);
		} else {
			Toast warning = Toast.makeText(this,
					R.string.toast_search_err,
					Toast.LENGTH_SHORT);
			warning.show();
		}
		
		searchTerm.setText(""); // clear search field
	}
	
	/*
	 * Start search on enter editor action.
	 */
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if (v.getId() == R.id.query_input && actionId == EditorInfo.IME_NULL) 
			startSearch();
		
		return false;
	}
	
	/*
	 * Handle user data on app destruction.
	 */
	public void onDestroy() {
		super.onDestroy();
		
		SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
		boolean persist = prefs.getBoolean("persist", false);
		
		if (!persist) {
			// clear login details
			Editor spe = prefs.edit();
			spe.clear();
			spe.commit();
		}
	}
	
	/*
	 * Prevent superclass menu from being displayed.
	 */
    public boolean onCreateOptionsMenu(Menu menu) {
    	return true;
    }
	
}