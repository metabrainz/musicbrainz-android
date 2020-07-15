package org.metabrainz.mobile.presentation.features.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import org.metabrainz.mobile.data.repository.SearchPagingSource;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();
    private final PagingConfig pagingConfig = new PagingConfig(20, 20, false);

    public SearchViewModel() {
    }

    void search(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty())
            searchQueryLiveData.setValue(searchTerm);
    }

    LiveData<PagingData<ResultItem>> getResultData(MBEntityType entity) {
        Pager<Integer, ResultItem> pager = new Pager<>(pagingConfig, () ->
                new SearchPagingSource(entity, searchQueryLiveData.getValue()));
        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager),
                ViewModelKt.getViewModelScope(this));
    }

}
