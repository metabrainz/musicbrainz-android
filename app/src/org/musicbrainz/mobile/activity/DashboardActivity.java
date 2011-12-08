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

import org.musicbrainz.android.api.webservice.HttpClient;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.suggestion.SuggestionHelper;
import org.musicbrainz.mobile.util.Constants;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.Menu;
import android.view.KeyEvent;
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

public class DashboardActivity extends MusicBrainzActivity implements OnEditorActionListener, OnItemClickListener {

    private static final int REQUEST_LOGIN = 0;

    private AutoCompleteTextView searchField;
    private Spinner searchTypeSpinner;
    private SuggestionHelper suggestionHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        findViews();

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.searchType,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(typeAdapter);
        suggestionHelper = new SuggestionHelper(this);
    }
    
    private void findViews() {
        searchField = (AutoCompleteTextView) findViewById(R.id.query_input);
        searchTypeSpinner = (Spinner) findViewById(R.id.search_spin);
        
        searchField.setOnEditorActionListener(this);
    }

    @Override
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

        if (isUserLoggedIn()) {
            login.setText(R.string.logout_label);
            messageBody.setText(getString(R.string.hometext_loggedin) + " " + getUsername());
        } else {
            login.setText(R.string.login_label);
            messageBody.setText(R.string.hometext);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_LOGIN && resultCode == LoginActivity.RESULT_LOGGED_IN) {
            setLoginStatus(true);
            Toast.makeText(this, R.string.toast_loggedIn, Toast.LENGTH_SHORT).show();
        } else if (requestCode == IntentIntegrator.BARCODE_REQUEST) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult.getContents() != null) {
                Intent barcodeResult = new Intent(this, ReleaseActivity.class);
                barcodeResult.putExtra(Extra.BARCODE, scanResult.getContents());
                startActivity(barcodeResult);
            }
        }
    }

    public void onClick(View view) {

        switch (view.getId()) {
        case R.id.login_btn:
            if (!isUserLoggedIn()) {
                startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
            } else {
                logOut();
                Toast.makeText(this, R.string.toast_loggedOut, Toast.LENGTH_SHORT).show();
                updateLoginState();
            }
            break;
        case R.id.scan_btn:
            // ZXing integration with external strings and product code mode
            IntentIntegrator.initiateScan(DashboardActivity.this, getString(R.string.zx_title),
                    getString(R.string.zx_message), getString(R.string.zx_pos), getString(R.string.zx_neg),
                    IntentIntegrator.PRODUCT_CODE_TYPES);
            break;
        case R.id.search_btn:
            startSearch();
            break;
        case R.id.about_btn:
            startActivity(new Intent(this, AboutActivity.class));
            break;
        case R.id.donate_btn:
            startActivity(new Intent(this, DonateActivity.class));
        }
    }

    private void logOut() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_USER, MODE_PRIVATE);
        Editor spe = prefs.edit();
        spe.clear();
        spe.commit();
        HttpClient.clearCredentials();
        setLoginStatus(false);
    }

    private void startSearch() {
        String query = searchField.getText().toString();

        if (query.length() > 0) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

            Intent searchIntent = new Intent(this, SearchActivity.class);
            searchIntent.putExtra(Extra.TYPE, getSearchTypeFromSpinner());
            searchIntent.putExtra(Extra.QUERY, query);
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
            return Extra.ARTIST;
        case 1:
            return Extra.RELEASE_GROUP;
        default:
            return Extra.ALL;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == R.id.query_input && actionId == EditorInfo.IME_NULL) {
            startSearch();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}