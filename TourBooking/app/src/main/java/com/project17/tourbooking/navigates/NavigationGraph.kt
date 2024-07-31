package com.project17.tourbooking.navigates

import BottomBar
import android.annotation.SuppressLint
import android.opengl.Visibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project17.tourbooking.activities.home.HomeScreen
import com.project17.tourbooking.activities.profile.ProfileScreen
import com.project17.tourbooking.activities.mytrip.MyTripScreen
import com.project17.tourbooking.activities.tripdetail.TripDetailScreen
import com.project17.tourbooking.activities.wishlist.WishListScreen

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun VisibilityBottomBarScaffold(
    navController: NavHostController,
    isBottomBarVisible: Boolean,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BottomBar(navController = navController)
            }
        }
    ) {
        content()
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, onBottomBarVisibilityChanged: (Boolean) -> Unit){
    NavHost(navController, startDestination = NavigationItems.Home.route, builder = {
        composable(NavigationItems.Home.route){
            onBottomBarVisibilityChanged(true)
            HomeScreen(navController)
        }
        composable(NavigationItems.MyTrip.route){
            onBottomBarVisibilityChanged(true)
            MyTripScreen()
        }
        composable(NavigationItems.WishList.route){
            onBottomBarVisibilityChanged(true)
            WishListScreen()
        }
        composable(NavigationItems.Profile.route){
            onBottomBarVisibilityChanged(true)
            ProfileScreen()
        }
    })
}