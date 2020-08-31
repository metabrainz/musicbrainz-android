package org.metabrainz.mobile.presentation.features.tagger

import android.net.Uri

interface OnItemCLickListener {
    fun onItemClicked(metadata: AudioFile?,uri: Uri?)
}
