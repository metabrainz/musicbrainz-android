package org.metabrainz.android.data.sources

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.metabrainz.android.data.sources.api.entities.mbentity.Collection
import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.android.data.sources.api.entities.response.CollectionListResponse
import org.metabrainz.android.util.Log.d
import java.util.*

object CollectionUtils {
    /*
     * The response from ws/2/collections has a field for count in all collections. Depending on the
     * entity, the name of the field can be artist-count, release-count etc. This method finds the
     * correct field name and assigns the value to count field in each collection. */
    fun setGenericCountParameter(jsonResponse: String?): MutableList<Collection> {
        val countList: MutableMap<String, String> = HashMap()
        val response = Gson().fromJson(jsonResponse, CollectionListResponse::class.java)
        val collections: MutableList<Collection> = ArrayList(response.collections)
        val jsonElement = JsonParser.parseString(jsonResponse)
        val result = jsonElement.asJsonObject.getAsJsonArray("collections")
        for (element in result) {
            val entries = element.asJsonObject.entrySet()
            var count = ""
            var id = ""
            for ((key, value) in entries) {
                if (key.contains("count")) count = value.asString
                if (key.equals("id", ignoreCase = true)) id = value.asString
            }
            d("$id $count")
            countList[id] = count
        }
        for (collection in collections) {
            val id = collection.mbid
            collection.count = Objects.requireNonNull(countList[id])!!.toInt()
        }
        return collections
    }

    fun removeCollections(collections: MutableList<Collection>) {
        val itr = collections.iterator()
        while (itr.hasNext()) {
            val entity = itr.next().entityType
            if (entity.equals("artist", ignoreCase = true) || entity.equals("release", ignoreCase = true)
                    || entity.equals("label", ignoreCase = true) || entity.equals("event", ignoreCase = true)
                    || entity.equals("instrument", ignoreCase = true) || entity.equals("recording", ignoreCase = true)
                    || entity.equals("release-group", ignoreCase = true)) continue
            itr.remove()
        }
    }

    fun getCollectionEntityType(collection: Collection): MBEntityType {
        return MBEntityType.valueOf(collection.entityType!!.replace('-', '_').uppercase(Locale.ROOT))
    }
}