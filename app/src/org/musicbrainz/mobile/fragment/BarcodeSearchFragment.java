package org.musicbrainz.mobile.fragment;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseInfo;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;
import org.musicbrainz.mobile.adapter.list.ReleaseInfoAdapter;
import org.musicbrainz.mobile.async.SearchReleaseLoader;
import org.musicbrainz.mobile.async.SubmitBarcodeLoader;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.dialog.ConfirmBarcodeDialog;
import org.musicbrainz.mobile.dialog.ConfirmBarcodeDialog.ConfirmBarcodeCallbacks;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class BarcodeSearchFragment extends Fragment implements OnEditorActionListener, OnClickListener,
        OnItemClickListener, OnItemLongClickListener, ConfirmBarcodeCallbacks, TextWatcher {

    private static final int SEARCH_RELEASE_LOADER = 0;
    private static final int SUBMIT_BARCODE_LOADER = 1;

    private EditText barcodeText;
    private EditText searchBox;
    private ImageButton searchButton;
    private TextView instructions;
    private TextView noResults;
    private ListView matches;
    private View loading;
    private View error;

    private String barcode;
    private List<ReleaseInfo> results;
    private int selection = 0;
    
    private LoadingCallbacks loadingCallbacks;
    
    public BarcodeSearchFragment() {
        setRetainInstance(true);
    }
    
    public interface LoadingCallbacks {
        public void startLoading();
        public void stopLoading();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        barcode = activity.getIntent().getStringExtra(Extra.BARCODE);
        try {
            loadingCallbacks = (LoadingCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + LoadingCallbacks.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_barcode_search, container, false);
        findViews(layout);
        setListeners();
        barcodeText.setText(barcode);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getLoaderManager().getLoader(SEARCH_RELEASE_LOADER) != null) {
            getLoaderManager().initLoader(SEARCH_RELEASE_LOADER, null, searchCallbacks);
        }
    }

    private void findViews(View layout) {
        searchBox = (EditText) layout.findViewById(R.id.barcode_search);
        barcodeText = (EditText) layout.findViewById(R.id.barcode);
        searchButton = (ImageButton) layout.findViewById(R.id.barcode_search_btn);
        matches = (ListView) layout.findViewById(R.id.barcode_list);
        instructions = (TextView) layout.findViewById(R.id.barcode_instructions);
        noResults = (TextView) layout.findViewById(R.id.noresults);
        loading = layout.findViewById(R.id.loading);
        error = layout.findViewById(R.id.error);
    }

    private void setListeners() {
        searchBox.setOnEditorActionListener(this);
        searchButton.setOnClickListener(this);
        barcodeText.addTextChangedListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.barcode_search && actionId == EditorInfo.IME_NULL) {
            doSearch();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        doSearch();
    }

    private void doSearch() {
        String term = searchBox.getText().toString();
        if (term.length() != 0) {
            hideKeyboard();
            prepareSearch();
            getLoaderManager().destroyLoader(SEARCH_RELEASE_LOADER);
            getLoaderManager().initLoader(SEARCH_RELEASE_LOADER, null, searchCallbacks);
        } else {
            Toast.makeText(App.getContext(), R.string.toast_search_err, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareSearch() {
        searchButton.setEnabled(false);
        instructions.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selection = position;
        showSubmitDialog();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent releaseIntent = new Intent(getActivity(), ReleaseActivity.class);
        releaseIntent.putExtra(Extra.RELEASE_MBID, results.get(position).getReleaseMbid());
        startActivity(releaseIntent);
        return true;
    }
    
    private void showSubmitDialog() {
        DialogFragment submitDialog = new ConfirmBarcodeDialog();
        submitDialog.show(getFragmentManager(), ConfirmBarcodeDialog.TAG);
    }

    private LoaderCallbacks<AsyncResult<List<ReleaseInfo>>> searchCallbacks = new LoaderCallbacks<AsyncResult<List<ReleaseInfo>>>() {

        @Override
        public Loader<AsyncResult<List<ReleaseInfo>>> onCreateLoader(int id, Bundle args) {
            return new SearchReleaseLoader(searchBox.getText().toString());
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<List<ReleaseInfo>>> loader, AsyncResult<List<ReleaseInfo>> data) {
            instructions.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.INVISIBLE);
            switch (data.getStatus()) {
            case SUCCESS:
                handleSearchResults(data);
                break;
            case EXCEPTION:
                showConnectionErrorWarning();
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<List<ReleaseInfo>>> loader) {
            loader.reset();
        }
    };

    private void showConnectionErrorWarning() {
        matches.setAdapter(null);
        error.setVisibility(View.VISIBLE);
        Button retry = (Button) error.findViewById(R.id.retry_button);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getLoaderManager().restartLoader(SEARCH_RELEASE_LOADER, null, searchCallbacks);
            }
        });
    }

    private void handleSearchResults(AsyncResult<List<ReleaseInfo>> result) {
        results = result.getData();
        matches.setAdapter(new ReleaseInfoAdapter(getActivity(), R.layout.list_release, results));
        matches.setOnItemClickListener(this);
        matches.setOnItemLongClickListener(this);

        error.setVisibility(View.GONE);
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
            return new SubmitBarcodeLoader(getSelectedReleaseMbid(), barcodeText.getText().toString());
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Void>> loader, AsyncResult<Void> data) {
            getLoaderManager().destroyLoader(SUBMIT_BARCODE_LOADER);
            loadingCallbacks.stopLoading();
            switch (data.getStatus()) {
            case EXCEPTION:
                Toast.makeText(App.getContext(), R.string.toast_barcode_fail, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toast.makeText(App.getContext(), R.string.toast_barcode, Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<Void>> loader) {
            loader.reset();
        }
    };

    private String getSelectedReleaseMbid() {
        return results.get(selection).getReleaseMbid();
    }
    
    @Override
    public ReleaseInfo getCurrentSelection() {
        return results.get(selection);
    }

    @Override
    public void confirmSubmission() {
        loadingCallbacks.startLoading();
        getLoaderManager().initLoader(SUBMIT_BARCODE_LOADER, null, submissionCallbacks);
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isDigits(s)) {
            barcodeText.setError(getString(R.string.barcode_invalid_chars));
        } else if (!isBarcodeLengthValid(s)) {
            barcodeText.setError(getString(R.string.barcode_invalid_length));
        } else {
            barcodeText.setError(null);
        }
    }
    
    private boolean isBarcodeLengthValid(CharSequence s) {
        return s.length() == 12 || s.length() == 13;
    }
    
    private boolean isDigits(CharSequence s) {
        String barcode = s.toString();
        char[] chars = barcode.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
