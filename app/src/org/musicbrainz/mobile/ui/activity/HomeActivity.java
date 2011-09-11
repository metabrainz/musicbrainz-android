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

package org.musicbrainz.mobile.ui.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.util.SuggestionHelper;
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Secrets;

import com.bugsense.trace.BugSenseHandler;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
public class HomeActivity extends SuperActivity implements OnEditorActionListener, OnItemClickListener {
	
	private AutoCompleteTextView searchField;
	private Spinner searchTypeSpinner;
	private InputMethodManager imm;
	private SuggestionHelper suggestionHelper;
	
	private static final int LOGIN_REQUEST = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
		if (Config.LIVE) {
			BugSenseHandler.setup(this, Secrets.BUGSENSE_API_KEY);
		}

		searchField = (AutoCompleteTextView) findViewById(R.id.query_input);
		searchField.setOnEditorActionListener(this);

		searchTypeSpinner = (Spinner) findViewById(R.id.search_spin);
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
				R.array.searchType, 
				android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchTypeSpinner.setAdapter(typeAdapter);
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		suggestionHelper = new SuggestionHelper(this);
	}
	
	public void onResume() {
		super.onResume();
		updateLoginState();
		
		if (shouldProvideSearchSuggestions()) {
			searchField.setAdapter(suggestionHelper.getAdapter());
			searchField.setOnItemClickListener(this);
		} else {
			searchField.setAdapter(suggestionHelper.getEmptyAdapter());
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		startSearch();
	}
	
	private void updateLoginState() {
		
		Button login = (Button) findViewById(R.id.login_btn);
		TextView messageBody = (TextView) findViewById(R.id.hometext);
		
		if (loggedIn) {
			login.setText(R.string.logout_label);
			messageBody.setText(getString(R.string.hometext_loggedin) + " " + getUsername());
		} else {
			login.setText(R.string.login_label);
			messageBody.setText(R.string.hometext);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if (requestCode == LOGIN_REQUEST) {
			if (resultCode == LoginActivity.LOGGED_IN) {
				loggedIn = true;
				Toast.makeText(this, R.string.toast_loggedIn, Toast.LENGTH_SHORT).show();
			} 
		} else if (requestCode == IntentIntegrator.BARCODE_REQUEST) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);
			
			if (scanResult.getContents() != null) {
				Intent barcodeResult = new Intent(this, ReleaseActivity.class);
				barcodeResult.putExtra("barcode", scanResult.getContents());
				startActivity(barcodeResult);
			}
		}
	}

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.login_btn:
			if (!loggedIn) {
				Intent logInIntent = new Intent(this, LoginActivity.class);
				startActivityForResult(logInIntent, LOGIN_REQUEST);
			} else {
				logOut();
				Toast.makeText(this, R.string.toast_loggedOut, Toast.LENGTH_SHORT).show();
				updateLoginState();
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
	
	private void logOut() {
		SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
		Editor spe = prefs.edit();
		spe.clear();
		spe.commit();
		loggedIn = false;
	}

	private void startSearch() {
		String query = searchField.getText().toString();

		if (query.length() > 0) {
			imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
			
			Intent searchIntent = new Intent(this, SearchActivity.class);
			searchIntent.putExtra(SearchActivity.INTENT_TYPE, getSearchTypeFromSpinner());
			searchIntent.putExtra(SearchActivity.INTENT_QUERY, query);
			startActivity(searchIntent);
		} else {
			Toast.makeText(this, R.string.toast_search_err, Toast.LENGTH_SHORT).show();
		}
		searchField.setText("");
	}
	
	private String getSearchTypeFromSpinner() {
		int spinnerPosition = searchTypeSpinner.getSelectedItemPosition();
		switch (spinnerPosition) {
		case 0:
			return SearchActivity.INTENT_ARTIST;
		case 1:
			return SearchActivity.INTENT_RELEASE_GROUP;
		default:
			return SearchActivity.INTENT_ALL;
		}
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if (v.getId() == R.id.query_input && actionId == EditorInfo.IME_NULL) {
			startSearch();
		}
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
			Editor spe = prefs.edit();
			spe.clear();
			spe.commit();
		}
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.home, menu);
    	return true;
    }
    
    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}