package org.metabrainz.mobile.presentation.features.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.SearchRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.adapters.ResultItemUtils;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private static SearchRepository repository = SearchRepository.getRepository();
    private MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();

    public SearchViewModel() {
    }

    void search(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty())
            searchQueryLiveData.setValue(searchTerm);
    }

    LiveData<List<ResultItem>> getResultData(MBEntityType entity) {
        return Transformations.map(
                Transformations.switchMap(searchQueryLiveData,
                        searchTerm -> repository.getResults(entity.name, searchTerm)),
                response -> ResultItemUtils.getJSONResponseAsResultItemList(response, entity));
    }

}
