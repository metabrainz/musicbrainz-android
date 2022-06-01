package org.metabrainz.android.presentation.features.critiques

sealed class CritiqueEntity(var entity: String,var displayName: String) {
    object AllReviews: CritiqueEntity("","All Reviews")
    object Artist : CritiqueEntity("artist", "Artist")
    object ReleaseGroup : CritiqueEntity("release_group", "Release Group")
    object Label : CritiqueEntity("label", "Label")
    object Recording : CritiqueEntity("recording", "Recording")
    object Event : CritiqueEntity("event", "Event")
    object Place : CritiqueEntity("place", "Place")
    object Work : CritiqueEntity("work", "Work")
}
