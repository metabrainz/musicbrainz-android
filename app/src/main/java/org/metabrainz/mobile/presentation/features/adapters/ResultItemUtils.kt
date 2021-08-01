package org.metabrainz.mobile.presentation.features.adapters

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.metabrainz.mobile.data.sources.api.entities.EntityUtils.getDisplayArtist
import org.metabrainz.mobile.data.sources.api.entities.mbentity.*
import java.lang.reflect.Type
import java.util.*

object ResultItemUtils {
    fun getEntityAsResultItem(entity: MBEntity?): ResultItem {
        val item: ResultItem? = when (entity) {
            is Artist -> {
                ResultItem(entity.mbid, entity.name, entity.disambiguation,
                    entity.type, entity.country)
            }
            is Event -> {
                val event = entity
                if (event.lifeSpan != null) ResultItem(event.mbid, event.name, event.disambiguation,
                    event.type, event.lifeSpan!!.timePeriod) else ResultItem(event.mbid, event.name, event.disambiguation,
                    event.type, "")
            }
            is Instrument -> {
                val instrument = entity
                ResultItem(instrument.mbid, instrument.name, instrument.disambiguation,
                    instrument.description, instrument.type)
            }
            is Label -> {
                val label = entity
                ResultItem(label.mbid, label.name, label.disambiguation,
                    label.type, label.country)
            }
            is Recording -> {
                if (entity.releases.size > 0) ResultItem(entity.mbid, entity.title, entity.disambiguation,
                    entity.releases[0].title, getDisplayArtist(entity.artistCredits)) else ResultItem(entity.mbid, entity.title, entity.disambiguation,
                    "", getDisplayArtist(entity.artistCredits))
            }
            is Release -> {
                ResultItem(entity.mbid, entity.title, entity.disambiguation,
                    getDisplayArtist(entity.artistCredits), entity.labelCatalog())
            }
            is ReleaseGroup -> {
                ResultItem(entity.mbid, entity.title, entity.disambiguation,
                    getDisplayArtist(entity.getArtistCredits()), entity.fullType)
            }
            else -> null
        }
        return item!!
    }

    private fun getTypeToken(entity: MBEntityType): Type? {
        return if (entity === MBEntityType.ARTIST) TypeToken.getParameterized(MutableList::class.java, Artist::class.java).type else if (entity === MBEntityType.RELEASE) TypeToken.getParameterized(MutableList::class.java, Release::class.java).type else if (entity === MBEntityType.LABEL) TypeToken.getParameterized(MutableList::class.java, Label::class.java).type else if (entity === MBEntityType.RECORDING) TypeToken.getParameterized(MutableList::class.java, Recording::class.java).type else if (entity === MBEntityType.EVENT) TypeToken.getParameterized(MutableList::class.java, Event::class.java).type else if (entity === MBEntityType.INSTRUMENT) TypeToken.getParameterized(MutableList::class.java, Instrument::class.java).type else if (entity === MBEntityType.RELEASE_GROUP) TypeToken.getParameterized(MutableList::class.java, ReleaseGroup::class.java).type else null
    }

    fun getJSONResponseAsResultItemList(response: String?, entity: MBEntityType): List<ResultItem> {
        val list = Gson().fromJson<List<MBEntity>>(JsonParser.parseString(response).asJsonObject[entity.entity + "s"], getTypeToken(entity))
        val items: MutableList<ResultItem> = ArrayList()
        for (e in list) items.add(getEntityAsResultItem(e))
        return items
    }
}