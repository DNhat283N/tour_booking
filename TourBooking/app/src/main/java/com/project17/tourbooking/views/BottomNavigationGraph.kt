package com.project17.tourbooking.views

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
    NavHost(navController, startDestination = BottomNavigationItems.Home.route){
        composable(BottomNavigationItems.Home.route){
            HomeScreen()
        }
        composable(BottomNavigationItems.MyTrip.route){
            MyTripScreen()
        }
        composable(BottomNavigationItems.WishList.route){
            WishListScreen()
        }
        composable(BottomNavigationItems.Profile.route){
            ProfileScreen()
        }
    }
}