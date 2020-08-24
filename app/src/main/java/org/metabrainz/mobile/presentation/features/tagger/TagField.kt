package org.metabrainz.mobile.presentation.features.tagger

data class TagField(val tagName: String, val originalValue: String = "", var newValue: String = "")