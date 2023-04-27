package org.metabrainz.android.model.response

import org.metabrainz.android.model.mbentity.Recording

class RecordingSearchResponse : SearchResponse<org.metabrainz.android.model.mbentity.MBEntity?>() {
    var recordings: List<Recording>? = null
}