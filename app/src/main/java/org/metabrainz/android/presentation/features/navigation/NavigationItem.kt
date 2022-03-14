package org.metabrainz.android.presentation.features.navigation

import org.metabrainz.android.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_musicbrainz_logo_no_text, "Home")
    object Listens : NavigationItem("listens", R.drawable.ic_listenbrainz_logo_no_text, "Listens")
    object Critiques : NavigationItem("critiques", R.drawable.ic_critiquebrainz_logo_no_text, "Critiques")
}
