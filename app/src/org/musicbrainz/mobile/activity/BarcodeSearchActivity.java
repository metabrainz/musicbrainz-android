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
import org.musicbrainz.mobile.activity.base.DataQueryActivity;
import org.musicbrainz.mobile.adapter.ReleaseStubAdapter;
import org.musicbrainz.mobile.dialog.BarcodeConfirmDialog;
import org.musicbrainz.mobile.task.SearchReleasesTask;
import org.musicbrainz.mobile.task.SubmitBarcodeTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Activity to submit a barcode to a selected release in MusicBrainz.
 */
public class BarcodeSearchActivity extends DataQueryActivity implements View.OnClickListener, ListView.OnItemClickListener,
        ListView.OnItemLongClickListener, OnEditorActionListener {

    private static final int DIALOG_SUBMIT_BARCODE = 1;
    
    private ActionBar actionBar;
    private TextView top;
    private EditText searchBox;
    private ImageButton search;

    private TextView instructions;
    private TextView noRes;
    private ListView matches;
    private LinearLayout loading;

    private String barcode;
    
    private SearchReleasesTask searchTask;
    private SubmitBarcodeTask submissionTask;
    
    private LinkedList<ReleaseStub> results;
    
    private ReleaseStub selection;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barcode);
        actionBar = setupActionBarWithHome();

        barcode = getIntent().getStringExtra(Extra.BARCODE);

        top = (TextView) findViewById(R.id.barcode_text);
        top.setText(top.getText() + " " + barcode);

        searchBox = (EditText) findViewById(R.id.barcode_search);
        searchBox.setOnEditorActionListener(this);

        search = (ImageButton) findViewById(R.id.barcode_search_btn);
        search.setOnClickListener(this);

        matches = (ListView) findViewById(R.id.barcode_list);
        instructions = (TextView) findViewById(R.id.barcode_instructions);
        noRes = (TextView) findViewById(R.id.noresults);
        loading = (LinearLayout) findViewById(R.id.loading);
        
        Object retained = getLastNonConfigurationInstance();
        if (retained instanceof TaskHolder) {
            TaskHolder holder = (TaskHolder) retained;
            reconnectTasks(holder);
        }
    }

    private void reconnectTasks(TaskHolder holder) {
        if (holder.searchTask != null) {
            searchTask = holder.searchTask;
            searchTask.connect(this);
            if (searchTask.isRunning()) preSearch();
            if (searchTask.isFinished()) onTaskFinished();
        }
        if (holder.submissionTask != null) {
            submissionTask = holder.submissionTask;
            submissionTask.connect(this);
            if (submissionTask.isRunning()) {
                onStartSubmission();
            }
        }
        selection = holder.selection;
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
        	searchTask = new SearchReleasesTask(this);
            searchTask.execute(term);
        } else {
            Toast.makeText(this, R.string.toast_search_err, Toast.LENGTH_SHORT).show();
        }
    }

    private void preSearch() {
        search.setEnabled(false);
        instructions.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
    }

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
	}

    public void submitBarcode(String releaseMbid) {
        submissionTask = new SubmitBarcodeTask(this, releaseMbid);
        submissionTask.execute(barcode);
    }
    
    public void onStartSubmission() {
        actionBar.setProgressBarVisibility(View.VISIBLE);
    }
    
    public void onSubmissionDone() {
        actionBar.setProgressBarVisibility(View.GONE);
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

    @Override
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
    public void onTaskFinished() {
        loading.setVisibility(View.INVISIBLE);
        if (searchTask.failed()) {
            showDialog(DIALOG_CONNECTION_FAILURE);
        } else {
            results = searchTask.getResults();
            
            matches.setAdapter(new ReleaseStubAdapter(this, results));
            matches.setOnItemClickListener(this);
            matches.setOnItemLongClickListener(this);

            instructions.setVisibility(View.INVISIBLE);
            if (results.isEmpty()) {
                noRes.setVisibility(View.VISIBLE);
                matches.setVisibility(View.INVISIBLE);
            } else {
                matches.setVisibility(View.VISIBLE);
                noRes.setVisibility(View.INVISIBLE);
            }
            search.setEnabled(true);
        }
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
    
    @Override
    public Object onRetainNonConfigurationInstance() {
        disconnectTasks();
        return new TaskHolder(searchTask, submissionTask, selection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectTasks();
    }

    private void disconnectTasks() {
        if (searchTask != null) searchTask.disconnect();
        if (submissionTask != null) submissionTask.disconnect();
    }

    private static class TaskHolder {

        public SearchReleasesTask searchTask;
        public SubmitBarcodeTask submissionTask;
        public ReleaseStub selection;

        public TaskHolder(SearchReleasesTask searchTask, SubmitBarcodeTask submissionTask, ReleaseStub selection) {
            this.searchTask = searchTask;
            this.submissionTask = submissionTask;
            this.selection = selection;
        }
    }

}

