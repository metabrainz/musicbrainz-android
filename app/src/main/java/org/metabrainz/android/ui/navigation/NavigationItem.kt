package org.metabrainz.android.ui.navigation

import org.metabrainz.android.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_house, "Home")
    object Profile : NavigationItem("profile", R.drawable.user, "Profile")
}