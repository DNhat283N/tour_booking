package com.project17.tourbooking.views

import com.project17.tourbooking.R

sealed class BottomNavigationItems(
    val route:String,
    val title: String,
    val icon: Int,
    val iconFocused: Int
) {
    object Home: BottomNavigationItems("home", "Home", R.drawable.ic_home, R.drawable.ic_home_focused)
    object MyTrip: BottomNavigationItems("mytrip", "My Trip", R.drawable.ic_mytrip, R.drawable.ic_mytrip_focused)
    object WishList: BottomNavigationItems("wishlist", "Wish List", R.drawable.ic_wishlist, R.drawable.ic_wishlist_focused)
    object Profile: BottomNavigationItems("profile", "Profile", R.drawable.ic_profile, R.drawable.ic_profile_focused)
}