package org.metabrainz.android.ui.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import org.metabrainz.android.util.Constants
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.ui.adapters.ResultItem

class SearchViewModel : ViewModel() {
    private val pagingConfig = PagingConfig(
        Constants.LIMIT,
            Constants.LIMIT / 5, false)

    fun search(entity: MBEntityType?, query: String?, offset: Int): LiveData<PagingData<ResultItem>> {
        val pager = Pager(pagingConfig, pagingSourceFactory={ SearchPagingSource(entity!!, query!!,offset) })
        return pager.liveData.cachedIn(this.viewModelScope)
    }
}