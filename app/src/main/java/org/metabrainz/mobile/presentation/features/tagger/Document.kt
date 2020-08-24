package org.metabrainz.mobile.presentation.features.tagger

import android.net.Uri

data class Document(
        val uri: Uri,
        val documentId: String,
        val displayName: String,
        val mimeType: String
)