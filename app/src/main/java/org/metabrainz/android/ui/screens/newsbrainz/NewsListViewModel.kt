package org.metabrainz.android.ui.screens.newsbrainz

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.repository.BlogRepository
import org.metabrainz.android.model.blog.Blog
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