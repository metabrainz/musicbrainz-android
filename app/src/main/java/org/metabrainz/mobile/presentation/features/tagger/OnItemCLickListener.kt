package org.metabrainz.mobile.presentation.features.tagger

import com.simplecityapps.ktaglib.AudioFile

interface OnItemCLickListener {
    fun onItemClicked(metadata: AudioFile?)
}
