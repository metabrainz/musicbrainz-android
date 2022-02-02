package org.metabrainz.android.presentation.features.search


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonParser
import org.metabrainz.android.data.sources.api.MusicBrainzServiceGenerator
import org.metabrainz.android.data.sources.api.SearchService
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.presentation.features.adapters.ResultItem
import org.metabrainz.android.presentation.features.adapters.ResultItemUtils

class SearchPagingSource(val mbEntityType: MBEntityType, val query: String) : PagingSource<Int, ResultItem>() {

    private val service = MusicBrainzServiceGenerator.createService(SearchService::class.java, true)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultItem> {
        val pageSize: Int = params.loadSize
        val offset = params.key ?: 0
        return try {
            val response = service.searchEntity(mbEntityType.entity, query, pageSize, offset)?.string()
            var count = LoadResult.Page.COUNT_UNDEFINED
            val entity = JsonParser.parseString(response).asJsonObject.get(mbEntityType.entity+"s").asJsonArray
            if (offset == 0) {
                val responseObject = JsonParser.parseString(response)
                count = responseObject.asJsonObject.get("count").asInt
            }
            val itemsAfter = when (count) {
                LoadResult.Page.COUNT_UNDEFINED -> count
                else -> (count - offset - pageSize).coerceAtLeast(0)
            }
            // itemsAfter is required to be at least otherwise the current page will be not loaded
            val nextKey = if (entity.isEmpty)  null
            else pageSize+offset

            if (offset==0 && entity.isEmpty && count==0){
                throw ArrayIndexOutOfBoundsException()
            }
            LoadResult.Page(
                data = ResultItemUtils.getJSONResponseAsResultItemList(response, mbEntityType),
                prevKey = null,
                nextKey = nextKey,
                itemsAfter = itemsAfter
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}