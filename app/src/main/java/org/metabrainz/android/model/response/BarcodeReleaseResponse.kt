package org.metabrainz.android.model.response

import org.metabrainz.android.model.mbentity.Release

class BarcodeReleaseResponse {
    var created: String? = null
    var count = 0
    var offset = 0
    var releases: List<Release> = ArrayList()
}