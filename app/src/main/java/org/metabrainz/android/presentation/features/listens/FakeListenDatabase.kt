package org.metabrainz.android.presentation.features.listens

import org.metabrainz.android.data.sources.api.entities.listens.AdditionalInfo
import org.metabrainz.android.data.sources.api.entities.listens.Listen
import org.metabrainz.android.data.sources.api.entities.listens.MbidMapping
import org.metabrainz.android.data.sources.api.entities.listens.TrackMetadata

object FakeListenDatabase {
    val listensList = listOf(
        Listen(
            "oak",
            1,
            "okan",
            TrackMetadata(
                AdditionalInfo(
                    "iasoas",
                    listOf("justin","pusha"),
                    90,12,
                    "pajsi",
                    "001",
                    "jsjiqs",
                    "oako",
                    listOf("opa","jai"),
                    "asnasa",
                    listOf("oaooaso","ysiwsi"),
                    "paosjw",
                    listOf("opqwqwdn","qsuqws"),
                    "aooa",
                    129
                ),
                "akko",
                MbidMapping(listOf("ajnja","Jjak"),
                    "oajs",
                    "oaanj"),
                "0osji",
                "kowjxo"),
            "Brown",
        ),
        Listen(
            "axosjcxoiwsjc",
            1,
            "okan",
            TrackMetadata(
                AdditionalInfo(
                    "iasoas",
                    listOf("aockoscjowc","pusha"),
                    90,12,
                    "pajsi",
                    "001",
                    "jsjiqs",
                    "oako",
                    listOf("opa","jai"),
                    "asnasa",
                    listOf("oaooaso","ysiwsi"),
                    "paosjw",
                    listOf("opqwqwdn","qsuqws"),
                    "aooa",
                    129
                ),
                "akko",
                MbidMapping(listOf("ajnja","Jjak"),
                    "oajs",
                    "oaanj"),
                "0osji",
                "kowjxo"),
            "Brown",
        ),
    )
}
