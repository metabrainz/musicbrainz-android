package org.metabrainz.android.presentation.features.navigation

import org.metabrainz.android.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_twotone_home_24, "Home")
    object News : NavigationItem("news", R.drawable.ic_twotone_new_releases_24, "News")
    object Listens : NavigationItem("listens", R.drawable.ic_twotone_music_video_24, "Listens")
    object Critiques : NavigationItem("critiques", R.drawable.ic_twotone_rate_review_24, "Critiques")
    object Profile : NavigationItem("profile", R.drawable.ic_twotone_person_24, "Profile")
}
