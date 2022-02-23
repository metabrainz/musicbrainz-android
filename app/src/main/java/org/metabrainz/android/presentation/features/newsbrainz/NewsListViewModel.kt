package org.metabrainz.android.presentation.features.newsbrainz

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import org.metabrainz.android.data.repository.BlogRepository
import org.metabrainz.android.data.sources.api.entities.blog.Blog
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(val repository: BlogRepository) : ViewModel() {
    fun fetchBlogs(): LiveData<Blog> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = repository.fetchBlogs()
            if (result.status == SUCCESS) {
                result.data?.let { emit(it) }
            }
        }
    }
}