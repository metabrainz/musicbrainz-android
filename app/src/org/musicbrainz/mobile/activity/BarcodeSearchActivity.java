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

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.ReleaseStubAdapter;
import org.musicbrainz.mobile.dialog.BarcodeConfirmDialog;
import org.musicbrainz.mobile.loader.AsyncResult;
import org.musicbrainz.mobile.loader.SearchReleaseLoader;
import org.musicbrainz.mobile.loader.SubmitBarcodeLoader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.Window;
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

/**
 * Activity to submit a barcode to a selected release in MusicBrainz.
 */
public class BarcodeSearchActivity extends MusicBrainzActivity implements View.OnClickListener,
        ListView.OnItemClickListener, ListView.OnItemLongClickListener, OnEditorActionListener {

    private static final int SEARCH_RELEASE_LOADER = 0;
    private static final int SUBMIT_BARCODE_LOADER = 1;

    private static final int DIALOG_CONNECTION_FAILURE = 0;
    private static final int DIALOG_SUBMIT_BARCODE = 1;

    private TextView barcodeText;
    private EditText searchBox;
    private ImageButton searchButton;

    private TextView instructions;
    private TextView noResults;
    private ListView matches;
    private LinearLayout loading;

    private String barcode;
    private String searchTerm;

    private LinkedList<ReleaseStub> results;
    private ReleaseStub selection;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_barcode);
        findViews();

        barcode = getIntent().getStringExtra(Extra.BARCODE);
        barcodeText.setText(barcodeText.getText() + " " + barcode);
    }

    private void findViews() {
        searchBox = (EditText) findViewById(R.id.barcode_search);
        barcodeText = (TextView) findViewById(R.id.barcode_text);
        searchButton = (ImageButton) findViewById(R.id.barcode_search_btn);
        matches = (ListView) findViewById(R.id.barcode_list);
        instructions = (TextView) findViewById(R.id.barcode_instructions);
        noResults = (TextView) findViewById(R.id.noresults);
        loading = (LinearLayout) findViewById(R.id.loading);

        searchBox.setOnEditorActionListener(this);
        searchButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        doSearch();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.barcode_search && actionId == EditorInfo.IME_NULL) {
            doSearch();
        }
        return false;
    }

    private void doSearch() {
        preSearch();
        String term = searchBox.getText().toString();
        if (term.length() != 0) {
            hideKeyboard();
            searchTerm = term;
            getSupportLoaderManager().initLoader(SEARCH_RELEASE_LOADER, null, searchCallbacks);
        } else {
            Toast.makeText(this, R.string.toast_search_err, Toast.LENGTH_SHORT).show();
        }
    }

    private void preSearch() {
        searchButton.setEnabled(false);
        instructions.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
    }

    public void submitBarcode(String releaseMbid) {
        setProgressBarIndeterminateVisibility(Boolean.TRUE);
        getSupportLoaderManager().initLoader(SUBMIT_BARCODE_LOADER, null, submissionCallbacks);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selection = results.get(position);
        showDialog(DIALOG_SUBMIT_BARCODE);
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent releaseIntent = new Intent(this, ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RELEASE_MBID, results.get(position).getReleaseMbid());
        startActivity(releaseIntent);
        return true;
    }

    protected Dialog createConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeSearchActivity.this);
        builder.setMessage(getString(R.string.err_text));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.err_pos), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doSearch();
                dialog.cancel();
                loading.setVisibility(View.VISIBLE);
            }
        });
        builder.setNegativeButton(getString(R.string.err_neg), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BarcodeSearchActivity.this.finish();
            }
        });
        return builder.create();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_SUBMIT_BARCODE:
            return new BarcodeConfirmDialog(this, selection);
        case DIALOG_CONNECTION_FAILURE:
            return createConnectionErrorDialog();
        }
        return null;
    }

    private LoaderCallbacks<AsyncResult<LinkedList<ReleaseStub>>> searchCallbacks = new LoaderCallbacks<AsyncResult<LinkedList<ReleaseStub>>>() {

        @Override
        public Loader<AsyncResult<LinkedList<ReleaseStub>>> onCreateLoader(int id, Bundle args) {
            return new SearchReleaseLoader(BarcodeSearchActivity.this, getUserAgent(), searchTerm);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<LinkedList<ReleaseStub>>> loader,
                AsyncResult<LinkedList<ReleaseStub>> data) {
            loading.setVisibility(View.INVISIBLE);
            switch (data.getStatus()) {
            case SUCCESS:
                handleSearchResults(data);
                break;
            case EXCEPTION:
                showDialog(DIALOG_CONNECTION_FAILURE);
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<LinkedList<ReleaseStub>>> loader) {
            loader.reset();
        }
    };

    private void handleSearchResults(AsyncResult<LinkedList<ReleaseStub>> result) {
        results = result.getData();
        matches.setAdapter(new ReleaseStubAdapter(this, results));
        matches.setOnItemClickListener(this);
        matches.setOnItemLongClickListener(this);

        instructions.setVisibility(View.INVISIBLE);
        if (results.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
            matches.setVisibility(View.INVISIBLE);
        } else {
            matches.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.INVISIBLE);
        }
        searchButton.setEnabled(true);
    }

    private LoaderCallbacks<AsyncResult<Void>> submissionCallbacks = new LoaderCallbacks<AsyncResult<Void>>() {

        @Override
        public Loader<AsyncResult<Void>> onCreateLoader(int id, Bundle args) {
            return new SubmitBarcodeLoader(BarcodeSearchActivity.this, getCredentials(), selection.getReleaseMbid(),
                    barcode);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Void>> loader, AsyncResult<Void> data) {
            getSupportLoaderManager().destroyLoader(SUBMIT_BARCODE_LOADER);
            setProgressBarIndeterminateVisibility(Boolean.FALSE);
            switch (data.getStatus()) {
            case EXCEPTION:
                Toast.makeText(BarcodeSearchActivity.this, R.string.toast_barcode_fail, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toast.makeText(BarcodeSearchActivity.this, R.string.toast_barcode, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<Void>> loader) {
            loader.reset();
        }
    };

}
