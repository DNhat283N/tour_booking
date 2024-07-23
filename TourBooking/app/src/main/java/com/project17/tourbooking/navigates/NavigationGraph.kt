package com.project17.tourbooking.navigates

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project17.tourbooking.activities.home.HomeScreen
import com.project17.tourbooking.activities.profile.ProfileScreen
import com.project17.tourbooking.activities.mytrip.MyTripScreen
import com.project17.tourbooking.activities.wishlist.WishListScreen

@Composable
fun BottomNavigationGraph(navController: NavHostController){
    NavHost(navController, startDestination = NavigationItems.Home.route){
        composable(NavigationItems.Home.route){
            HomeScreen()
        }
        composable(NavigationItems.MyTrip.route){
            MyTripScreen()
        }
        composable(NavigationItems.WishList.route){
            WishListScreen()
        }
        composable(NavigationItems.Profile.route){
            ProfileScreen()
        }
    }
}