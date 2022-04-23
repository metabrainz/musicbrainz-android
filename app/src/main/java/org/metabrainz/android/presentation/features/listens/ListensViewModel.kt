package org.metabrainz.android.presentation.features.listens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.metabrainz.android.data.repository.ListensRepository
import org.metabrainz.android.data.sources.api.entities.CoverArt
import org.metabrainz.android.data.sources.api.entities.listens.Listen
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class ListensViewModel @Inject constructor(val repository: ListensRepository) : ViewModel() {
    var listens: List<Listen> by mutableStateOf(listOf())
    var coverArt: CoverArt? by mutableStateOf(null)

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

    fun fetchCoverArt(MBID: String) {
        viewModelScope.launch {
            val response = repository.fetchCoverArt(MBID)
            when(response.status){
                Resource.Status.SUCCESS -> {
                    coverArt = response.data!!
                }
                else -> {

                }
            }
        }
    }
}