package org.metabrainz.mobile.presentation.features.dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.databinding.FragmentDashSearchBinding;
import org.metabrainz.mobile.presentation.features.search.SearchActivity;
import org.metabrainz.mobile.presentation.features.suggestion.SuggestionHelper;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentDashSearchBinding binding;
    private SuggestionHelper suggestionHelper;
    private CursorAdapter suggestionAdapter;
    private View clearFocusView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashSearchBinding.inflate(inflater, container, false);

        //Work around to prevent keyboard from auto showing
        binding.clearFocusView.requestFocus();

        setupSearchView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSearchTypeSpinner();
        suggestionHelper = new SuggestionHelper(getActivity());
        suggestionAdapter = suggestionHelper.getAdapter();
        binding.searchView.setSuggestionsAdapter(suggestionAdapter);
    }

    private void setupSearchTypeSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.searchType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.searchSpin.setAdapter(typeAdapter);
    }

    private void startSearch() {
        String query = binding.searchView.getQuery().toString();

        if (query.length() > 0) {
            Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
            searchIntent.putExtra(SearchManager.QUERY, query);
            searchIntent.putExtra(Constants.TYPE, getSearchTypeFromSpinner());
            startActivity(searchIntent);
        } else {
            Toast.makeText(getActivity(), R.string.toast_search_err, Toast.LENGTH_SHORT).show();
        }
    }

    private MBEntityType getSearchTypeFromSpinner() {
        int spinnerPosition = binding.searchSpin.getSelectedItemPosition();
        switch (spinnerPosition) {
            case 0:
                return MBEntityType.ARTIST;
            case 1:
                return MBEntityType.RELEASE;
            case 2:
                return MBEntityType.LABEL;
            case 3:
                return MBEntityType.RECORDING;
            case 4:
                return MBEntityType.RELEASE_GROUP;
            case 5:
                return MBEntityType.INSTRUMENT;
            case 6:
                return MBEntityType.EVENT;
            default:
                return null;
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        binding.searchView.setSubmitButtonEnabled(true);
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setOnQueryTextListener(this);
        binding.searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) suggestionAdapter.getItem(position);
                String query = cursor.getString(cursor.getColumnIndexOrThrow("display1"));
                binding.searchView.setQuery(query, false);
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
