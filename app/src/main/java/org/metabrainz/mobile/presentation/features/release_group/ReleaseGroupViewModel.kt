package org.metabrainz.mobile.presentation.features.release_group

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup
import org.metabrainz.mobile.presentation.features.base.LookupViewModel
import org.metabrainz.mobile.util.Resource

class ReleaseGroupViewModel @ViewModelInject constructor(repository: LookupRepository) :
        LookupViewModel<ReleaseGroup>(repository, MBEntityType.RELEASE_GROUP) {

    val wikiData: LiveData<Resource<WikiSummary>>
    override val data: LiveData<Resource<ReleaseGroup>> = jsonLiveData.map { parseData(it) }

    private suspend fun fetchWikiSummary(resource: Resource<ReleaseGroup>): Resource<WikiSummary> {
        if (resource.status == Resource.Status.SUCCESS) {
            var title = ""
            var method = -1
            for (link in resource.data.relations) {
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
            if (title.isNotEmpty())
                return repository.fetchWikiSummary(title, method)
        }
        return Resource.getFailure(WikiSummary::class.java)
    }

    init {
        wikiData = data.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(fetchWikiSummary(it))
            }
        }
    }
}