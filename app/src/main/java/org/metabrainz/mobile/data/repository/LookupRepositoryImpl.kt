package org.metabrainz.mobile.data.repository

import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.metabrainz.mobile.data.sources.api.LookupService
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.WikiDataResponse
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LookupRepositoryImpl @Inject constructor(private val service: LookupService) : LookupRepository {

    @WorkerThread
    override suspend fun fetchData(entity: String, MBID: String, params: String): Resource<String> {
        return try {
            val data = service.lookupEntityData(entity, MBID, params)
            Resource(SUCCESS, data.string())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(String::class.java)
        }
    }

    @WorkerThread
    override suspend fun fetchWikiSummary(string: String, method: Int): Resource<WikiSummary> {
        return if (method == LookupRepository.METHOD_WIKIPEDIA_URL)
            fetchWiki(string)
        else
            fetchWikiData(string)
    }

    @WorkerThread
    private suspend fun fetchWiki(title: String): Resource<WikiSummary> {
        return try {
            val data = service.getWikipediaSummary(title)
            Resource(SUCCESS, data)
        } catch (e: Exception) {
            Resource.getFailure(WikiSummary::class.java)
        }
    }

    @WorkerThread
    private suspend fun fetchWikiData(id: String): Resource<WikiSummary> {
        return try {
            val responseBody = service.getWikipediaLink(id)
            val jsonResponse = responseBody.string()
            val result = JsonParser.parseString(jsonResponse).asJsonObject
                    .getAsJsonObject("entities").getAsJsonObject(id)
            val wikiDataResponse = Gson().fromJson(result, WikiDataResponse::class.java)
            val title = wikiDataResponse.sitelinks["enwiki"]?.title
            if (title != null)
                fetchWiki(title)
            else
                Resource.getFailure(WikiSummary::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.getFailure(WikiSummary::class.java)
        }
    }

    @WorkerThread
    override suspend fun fetchCoverArt(MBID: String): Resource<CoverArt> {
        return try {
            val coverArt = service.getCoverArt(MBID)
            Resource(SUCCESS, coverArt)
        } catch (e: Exception) {
            Resource.getFailure(CoverArt::class.java)
        }
    }

}