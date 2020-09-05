package org.metabrainz.mobile.presentation.features.artist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType.ARTIST
import org.metabrainz.mobile.presentation.features.LookupViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

class ArtistViewModel @ViewModelInject constructor(repository: LookupRepository)
    : LookupViewModel(repository, ARTIST) {

    val wikiData: LiveData<Resource<WikiSummary>>

    override val data: LiveData<Resource<Artist>>

    private suspend fun fetchWikiSummary(resource: Resource<Artist>): Resource<WikiSummary> {
        if (resource.status == SUCCESS) {
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

    private fun toArtist(data: Resource<String>): Resource<Artist> {
        return try {
            if (data.status == SUCCESS)
                Resource(SUCCESS, Gson().fromJson(data.data, Artist::class.java))
            else
                Resource.getFailure(Artist::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(Artist::class.java)
        }
    }

    init {
        data = jsonLiveData.map { toArtist(it) }
        wikiData = data.switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(fetchWikiSummary(it))
            }
        }
    }

}