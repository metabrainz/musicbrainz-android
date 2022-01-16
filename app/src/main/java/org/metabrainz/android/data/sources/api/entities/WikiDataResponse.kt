package org.metabrainz.android.data.sources.api.entities

class WikiDataResponse {
    var sitelinks: Map<String, WikiDataEntry>? = null
    var type: String? = null
    var id: String? = null
}