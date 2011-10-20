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

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.WebClient;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.ReleaseStubAdapter;
import org.musicbrainz.mobile.dialog.BarcodeConfirmDialog;
import org.musicbrainz.mobile.util.Log;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

/**
 * Activity to submit a barcode to a selected release in MusicBrainz if isn't
 * already stored.
 */
public class BarcodeSearchActivity extends SuperActivity implements View.OnClickListener, ListView.OnItemClickListener,
        ListView.OnItemLongClickListener, OnEditorActionListener {

    private ActionBar actionBar;
    private TextView top;
    private EditText searchBox;
    private ImageButton search;

    private TextView noRes;
    private ListView matches;

    private String barcode;
    private LinkedList<ReleaseStub> results;

    private LinearLayout loading;

    private InputMethodManager imm;

    private WebClient webService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = new WebClient(generateUserAgent());

        setContentView(R.layout.activity_barcode);
        actionBar = setupActionBarWithHome();

        barcode = getIntent().getStringExtra("barcode");

        top = (TextView) findViewById(R.id.barcode_text);
        top.setText(top.getText() + " " + barcode);

        searchBox = (EditText) findViewById(R.id.barcode_search);
        searchBox.setOnEditorActionListener(this);

        search = (ImageButton) findViewById(R.id.barcode_search_btn);
        search.setOnClickListener(this);

        matches = (ListView) findViewById(R.id.barcode_list);
        noRes = (TextView) findViewById(R.id.noresults);
        loading = (LinearLayout) findViewById(R.id.loading);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * Task which performs release search and displays results.
     */
    private class SearchTask extends AsyncTask<Void, Void, Boolean> {

        protected void onPreExecute() {
            search.setEnabled(false);
            TextView instructions = (TextView) findViewById(R.id.barcode_instructions);
            instructions.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }

        protected Boolean doInBackground(Void... arg0) {
            try {
                results = webService.searchRelease(searchBox.getText().toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {

            if (success) {
                matches.setAdapter(new ReleaseStubAdapter(BarcodeSearchActivity.this, results));
                matches.setOnItemClickListener(BarcodeSearchActivity.this);
                matches.setOnItemLongClickListener(BarcodeSearchActivity.this);

                if (results.isEmpty()) {
                    noRes.setVisibility(View.VISIBLE);
                    matches.setVisibility(View.INVISIBLE);
                } else {
                    matches.setVisibility(View.VISIBLE);
                    noRes.setVisibility(View.INVISIBLE);
                }
                search.setEnabled(true);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeSearchActivity.this);
                builder.setMessage(getString(R.string.err_text)).setCancelable(false)
                        .setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // restart search
                                new SearchTask().execute();
                                dialog.cancel();
                                toggleLoading();
                            }
                        }).setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish activity
                                BarcodeSearchActivity.this.finish();
                            }
                        });
                try {
                    Dialog conError = builder.create();
                    conError.show();
                } catch (Exception e) {
                    Log.e("Connection timed out but Activity has closed anyway");
                }
            }
            toggleLoading();
        }
    }

    private void toggleLoading() {
        if (loading.getVisibility() == View.INVISIBLE) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.INVISIBLE);
        }
    }

    /*
     * Listener for button clicks.
     */
    public void onClick(View v) {
        doSearch();
    }

    /*
     * Start search on enter editor action.
     */
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == R.id.barcode_search && actionId == EditorInfo.IME_NULL) {
            doSearch();
        }
        return false;
    }

    private void doSearch() {

        String term = searchBox.getText().toString();
        if (term.length() != 0) {
            // hide virtual keyboard
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            new SearchTask().execute();
        } else {
            Toast.makeText(this, R.string.toast_search_err, Toast.LENGTH_SHORT).show();
        }
    }

    public void submitBarcode(String releaseMbid) {
        new SubmitBarcodeTask().execute(releaseMbid);
    }

    private class SubmitBarcodeTask extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {
            actionBar.setProgressBarVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... releaseMbid) {

            try {
                webService.setCredentials(getUsername(), getPassword());
                webService.setClientId(generateClientId());
                webService.submitBarcode(releaseMbid[0], barcode);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {

            actionBar.setProgressBarVisibility(View.GONE);

            if (success) {
                Toast.makeText(BarcodeSearchActivity.this, R.string.toast_barcode, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BarcodeSearchActivity.this, R.string.toast_barcode_fail, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        new BarcodeConfirmDialog(this, results.get(position)).show();
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Intent releaseIntent = new Intent(this, ReleaseActivity.class);
        releaseIntent.putExtra("r_id", results.get(position).getReleaseMbid());
        startActivity(releaseIntent);

        return true;
    }

}
