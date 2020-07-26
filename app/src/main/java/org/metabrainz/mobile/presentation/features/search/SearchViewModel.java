package org.metabrainz.mobile.presentation.features.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import org.metabrainz.mobile.data.repository.SearchPagingSource;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;

public class SearchViewModel extends ViewModel {

    private final PagingConfig pagingConfig = new PagingConfig(Constants.LIMIT,
            Constants.LIMIT / 5, false);

    public SearchViewModel() {
    }

    LiveData<PagingData<ResultItem>> search(MBEntityType entity, String query) {
        Pager<Integer, ResultItem> pager = new Pager<>(pagingConfig, () ->
                new SearchPagingSource(entity, query));
        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager),
                ViewModelKt.getViewModelScope(this));
    }

}
