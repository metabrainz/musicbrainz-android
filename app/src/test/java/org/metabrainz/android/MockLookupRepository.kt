package org.metabrainz.android

import com.google.gson.Gson
import org.metabrainz.android.EntityTestUtils.loadResourceAsString
import org.metabrainz.android.repository.LookupRepository
import org.metabrainz.android.model.entities.CoverArt
import org.metabrainz.android.model.entities.RecordingItem
import org.metabrainz.android.model.entities.WikiSummary
import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.SUCCESS

class MockLookupRepository : LookupRepository {

    override suspend fun fetchData(entity: String, MBID: String, params: String?): Resource<String> {
        return Resource(SUCCESS, loadResourceAsString(entity + "_lookup.json"))
    }

    override suspend fun fetchWikiSummary(string: String, method: Int): Resource<WikiSummary> {
        val response = loadResourceAsString("artist_wiki.json")
        val summary = Gson().fromJson(response, WikiSummary::class.java)
        return Resource(SUCCESS, summary)
    }

    override suspend fun fetchCoverArt(MBID: String?): Resource<org.metabrainz.android.model.entities.CoverArt> {
        val response = loadResourceAsString("cover_art.json")
        val coverArt = Gson().fromJson(response, org.metabrainz.android.model.entities.CoverArt::class.java)
        return Resource(SUCCESS, coverArt)
    }

    override suspend fun fetchRecordings(artist: String?, title: String?): Resource<List<RecordingItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMatchedRelease(MBID: String?): Resource<Release> {
        TODO("Not yet implemented")
    }
}