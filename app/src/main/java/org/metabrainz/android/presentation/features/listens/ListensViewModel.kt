package org.metabrainz.android.presentation.features.listens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.metabrainz.android.data.repository.CollectionRepository
import org.metabrainz.android.data.repository.ListensRepository
import org.metabrainz.android.data.sources.api.entities.listens.Listen
import org.metabrainz.android.data.sources.api.entities.mbentity.Collection
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.presentation.features.adapters.ResultItem
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Utils.toResultItemsList
import javax.inject.Inject

@HiltViewModel
class ListensViewModel @Inject constructor(val repository: ListensRepository) : ViewModel() {
    var listens: List<Listen> by mutableStateOf(listOf())

    init {
        fetchUserListens("akshaaatt")
    }

    fun fetchUserListens(userName: String) {
        viewModelScope.launch {
            val response = repository.fetchUserListens(userName)
            when(response.status){
                Resource.Status.SUCCESS -> {
                    listens = response.data!!
                }
                else -> {

                }
            }
        }
    }
}