package com.project17.tourbooking.navigates

import com.project17.tourbooking.R

sealed class NavigationItems(
    val route:String,
    val title: String,
    val icon: Int,
    val iconFocused: Int
) {
    object Home: NavigationItems("home", "Home", R.drawable.ic_home, R.drawable.ic_home_focused)
    object MyTrip: NavigationItems("mytrip", "My Trip", R.drawable.ic_mytrip, R.drawable.ic_mytrip_focused)
    object WishList: NavigationItems("wishlist", "Wish List", R.drawable.ic_wishlist, R.drawable.ic_wishlist_focused)
    object Profile: NavigationItems("profile", "Profile", R.drawable.ic_profile, R.drawable.ic_profile_focused)
}