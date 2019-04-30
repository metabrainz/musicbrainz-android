package org.metabrainz.mobile.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.fragment.ContextFragment;
import org.metabrainz.mobile.intent.IntentFactory.Extra;
import org.metabrainz.mobile.presentation.features.search.SearchActivity;
import org.metabrainz.mobile.suggestion.SuggestionHelper;

public class SearchFragment extends ContextFragment implements SearchView.OnQueryTextListener {

    private Spinner searchTypeSpinner;
    private SuggestionHelper suggestionHelper;
    private SearchView searchView;
    private CursorAdapter suggestionAdapter;
    private View clearFocusView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dash_search, container);
        searchTypeSpinner = layout.findViewById(R.id.search_spin);
        searchView = layout.findViewById(R.id.search_view);

        //Work around to prevent keyboard from auto showing
        clearFocusView = layout.findViewById(R.id.clear_focus_view);
        clearFocusView.requestFocus();

        setupSearchView();
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSearchTypeSpinner();
        suggestionHelper = new SuggestionHelper(getActivity());
        suggestionAdapter = suggestionHelper.getAdapter();
        searchView.setSuggestionsAdapter(suggestionAdapter);
    }

    private void setupSearchTypeSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.searchType,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(typeAdapter);
    }

    private void startSearch() {
        String query = searchView.getQuery().toString();

        if (query.length() > 0) {
            Intent searchIntent = new Intent(context, SearchActivity.class);
            searchIntent.putExtra(Extra.TYPE, getSearchTypeFromSpinner());
            searchIntent.putExtra(Extra.QUERY, query);
            startActivity(searchIntent);
        } else {
            Toast.makeText(context, R.string.toast_search_err, Toast.LENGTH_SHORT).show();
        }
    }

    private String getSearchTypeFromSpinner() {
        int spinnerPosition = searchTypeSpinner.getSelectedItemPosition();
        switch (spinnerPosition) {
            case 0:
                return Extra.ARTIST;
            case 1:
                return Extra.RELEASE;
            case 2:
                return Extra.LABEL;
            case 3:
                return Extra.RECORDING;
            case 4:
                return Extra.RELEASE_GROUP;
            case 5:
                return Extra.INSTRUMENT;
            case 6:
                return Extra.EVENT;
            default:
                return Extra.ALL;
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) suggestionAdapter.getItem(position);
                String query = cursor.getString(cursor.getColumnIndexOrThrow("display1"));
                searchView.setQuery(query, false);
                return true;
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startSearch();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        suggestionAdapter.changeCursor(suggestionHelper.getMatchingEntries(newText));
        return false;
    }
}
