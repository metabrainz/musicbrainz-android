package org.metabrainz.android.presentation.features.critiques


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.metabrainz.android.data.repository.CritiquesRepository
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.sources.api.entities.critiques.Review
import org.metabrainz.android.presentation.features.critiques.CritiqueEntity.*
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class CritiquesViewModel @Inject constructor(val repository: CritiquesRepository, val lookupRepository: LookupRepository) : ViewModel() {
    var reviews: List<Review> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    var reviewTitle: String by mutableStateOf("")

    fun fetchAllReviews() {
        viewModelScope.launch {
            val response = repository.fetchAllReviews()
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    isLoading = false
                    reviews = response.data!!
                }
                Resource.Status.LOADING -> {
                    isLoading = true
                }
                Resource.Status.FAILED -> {
                    isLoading = false
                }
            }
            return@launch
        }
    }

    fun fetchReviews(entity: String) {
        viewModelScope.launch {
            val response = when (entity) {
                Artist.entity -> repository.fetchArtistReviews()
                ReleaseGroup.entity -> repository.fetchReleaseGroupReviews()
                Label.entity -> repository.fetchLabelReviews()
                Recording.entity -> repository.fetchRecordingReviews()
                Event.entity -> repository.fetchEventReviews()
                Place.entity -> repository.fetchPlaceReviews()
                Work.entity -> repository.fetchWorkReviews()
                else -> repository.fetchAllReviews()
            }
            reviews = response.data!!
            return@launch
        }
    }
}