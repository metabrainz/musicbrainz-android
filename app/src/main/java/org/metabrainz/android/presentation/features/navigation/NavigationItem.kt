package org.metabrainz.android.presentation.features.navigation

import org.metabrainz.android.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_house, "Home")
    object News : NavigationItem("news", R.drawable.ic_news, "News")
    object Listens : NavigationItem("listens", R.drawable.ic_listen, "Listens")
    object Critiques : NavigationItem("critiques", R.drawable.ic_film_review, "Critiques")
    object Profile : NavigationItem("profile", R.drawable.user, "Profile")
}
