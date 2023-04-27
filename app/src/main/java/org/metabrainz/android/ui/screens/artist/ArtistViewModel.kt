package org.metabrainz.android.ui.screens.artist

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.model.entities.WikiSummary
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.ui.screens.base.LookupViewModel
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(repository: LookupRepository) : LookupViewModel<org.metabrainz.android.model.mbentity.Artist>(repository,
    MBEntityType.ARTIST
) {

    val wikiData: LiveData<Resource<WikiSummary>>

    override val data: LiveData<Resource<org.metabrainz.android.model.mbentity.Artist>> = jsonLiveData.map { parseData(it) }

    private suspend fun fetchWikiSummary(resource: Resource<org.metabrainz.android.model.mbentity.Artist>): Resource<WikiSummary> {
        if (resource.status == SUCCESS) {
            var title = ""
            var method = -1
            for (link in resource.data!!.relations) {
                if (link.type == "wikipedia") {
                    title = link.pageTitle
                    method = LookupRepository.METHOD_WIKIPEDIA_URL
                    break
                }
                if (link.type == "wikidata") {
                    title = link.pageTitle
                    method = LookupRepository.METHOD_WIKIDATA_ID
                    break
                }
            }
            if (title.isNotEmpty()) {
                return repository.fetchWikiSummary(title, method)
            }
        }
        return Resource.failure()
    }

    init {
        wikiData = data.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(fetchWikiSummary(it))
            }
        }
    }

}