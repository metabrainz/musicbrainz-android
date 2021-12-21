package org.metabrainz.android.presentation.features.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import org.metabrainz.android.data.sources.Constants
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.presentation.features.adapters.ResultItem

class SearchViewModel : ViewModel() {
    private val pagingConfig = PagingConfig(Constants.LIMIT,
            Constants.LIMIT / 5, false)

    fun search(entity: MBEntityType?, query: String?): LiveData<PagingData<ResultItem>> {
        val pager = Pager(pagingConfig, pagingSourceFactory={ SearchPagingSource(entity!!, query!!) })
        return pager.liveData.cachedIn(this.viewModelScope)
    }
}