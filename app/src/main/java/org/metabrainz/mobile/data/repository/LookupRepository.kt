package org.metabrainz.mobile.data.repository

import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary
import org.metabrainz.mobile.util.Resource

interface LookupRepository {

    suspend fun fetchData(entity: String, MBID: String, params: String): Resource<String>

    suspend fun fetchWikiSummary(string: String, method: Int): Resource<WikiSummary>

    suspend fun fetchCoverArt(MBID: String): Resource<CoverArt>

    companion object {
        const val METHOD_WIKIPEDIA_URL = 0
        const val METHOD_WIKIDATA_ID = 1
    }
}