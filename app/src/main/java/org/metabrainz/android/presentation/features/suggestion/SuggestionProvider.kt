package org.metabrainz.android.presentation.features.suggestion

import android.content.SearchRecentSuggestionsProvider
import org.metabrainz.android.BuildConfig

class SuggestionProvider : SearchRecentSuggestionsProvider() {
    companion object {
        const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.SearchSuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}