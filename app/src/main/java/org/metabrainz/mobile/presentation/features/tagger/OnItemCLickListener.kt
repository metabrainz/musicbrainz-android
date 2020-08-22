package org.metabrainz.mobile.presentation.features.tagger

import com.simplecityapps.ktaglib.AudioFile

public interface OnItemCLickListener {
    fun onItemClicked(metadata: AudioFile?)

}
