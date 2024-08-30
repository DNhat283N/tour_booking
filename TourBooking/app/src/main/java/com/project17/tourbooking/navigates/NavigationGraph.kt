package com.project17.tourbooking.navigates

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project17.tourbooking.activities.home.HomeScreen
import com.project17.tourbooking.activities.search.SearchFilterScreen
import com.project17.tourbooking.activities.search.SearchScreen
import com.project17.tourbooking.activities.tripdetail.TripDetailScreen
import com.project17.tourbooking.viewmodels.AppViewModel

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
fun NavigationGraph(navController: NavHostController, onBottomBarVisibilityChanged: (Boolean) -> Unit, appViewModel: AppViewModel = viewModel()){
    NavHost(navController, startDestination = NavigationItems.Home.route, builder = {
        composable(NavigationItems.Home.route){
            onBottomBarVisibilityChanged(true)
            HomeScreen(navController = navController, appViewModel = appViewModel)
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
        composable(NavigationItems.TripDetail.route + "/{tourId}"){

        composable(NavigationItems.TripDetail.route){

            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            TripDetailScreen(navController, tourId ?: "")
        }
        composable(NavigationItems.Search.route){
            onBottomBarVisibilityChanged(false)
            SearchScreen(navController, appViewModel)
        }
        composable(NavigationItems.SearchFilter.route){
            onBottomBarVisibilityChanged(false)
            SearchFilterScreen(navController, appViewModel)
        }
    })
}