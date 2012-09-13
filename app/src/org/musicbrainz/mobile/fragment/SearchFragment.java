package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.SearchActivity;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;
import org.musicbrainz.mobile.suggestion.SuggestionHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchFragment extends ContextFragment implements OnEditorActionListener, OnItemClickListener,
        OnClickListener {

    private AutoCompleteTextView searchField;
    private Spinner searchTypeSpinner;
    private SuggestionHelper suggestionHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dash_search, container);
        searchTypeSpinner = (Spinner) layout.findViewById(R.id.search_spin);
        searchField = (AutoCompleteTextView) layout.findViewById(R.id.query_input);
        searchField.setOnEditorActionListener(this);
        layout.findViewById(R.id.search_btn).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSearchTypeSpinner();
        suggestionHelper = new SuggestionHelper(getActivity());
    }

    private void setupSearchTypeSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.searchType,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(typeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.getUser().isSearchSuggestionsEnabled()) {
            searchField.setAdapter(suggestionHelper.getAdapter());
            searchField.setOnItemClickListener(this);
        } else {
            searchField.setAdapter(suggestionHelper.getEmptyAdapter());
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startSearch();
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn) {
            startSearch();
        }
    }

    private void startSearch() {
        String query = searchField.getText().toString();

        if (query.length() > 0) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

            Intent searchIntent = new Intent(context, SearchActivity.class);
            searchIntent.putExtra(Extra.TYPE, getSearchTypeFromSpinner());
            searchIntent.putExtra(Extra.QUERY, query);
            startActivity(searchIntent);
        } else {
            Toast.makeText(context, R.string.toast_search_err, Toast.LENGTH_SHORT).show();
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
        case 2:
            return Extra.LABEL;
        case 3:
            return Extra.RECORDING;
        default:
            return Extra.ALL;
        }
    }

}
