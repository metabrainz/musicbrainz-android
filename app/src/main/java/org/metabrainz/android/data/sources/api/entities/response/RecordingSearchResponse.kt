package org.metabrainz.android.data.sources.api.entities.response

import org.metabrainz.android.data.sources.api.entities.mbentity.MBEntity
import org.metabrainz.android.data.sources.api.entities.mbentity.Recording

class RecordingSearchResponse : SearchResponse<MBEntity?>() {
    var recordings: List<Recording>? = null
}