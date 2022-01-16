package org.metabrainz.android.presentation.features.newsbrainz

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.data.repository.BlogRepository
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.sources.api.entities.CoverArt
import org.metabrainz.android.data.sources.api.entities.mbentity.Release
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(val repository: BlogRepository) : ViewModel() {
    fun fetchBlogs(): LiveData<String> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = repository.fetchBlogs()
            if (result.status == SUCCESS) {
                emit(result.data!!)
            }
        }
    }
}