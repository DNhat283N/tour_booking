package com.project17.tourbooking.navigates

import BottomBar
import MyTripActivity
import ProfileActivity
import YourWishlist
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project17.tourbooking.activities.home.HomeScreen
import com.project17.tourbooking.activities.search.SearchBarSection
import com.project17.tourbooking.activities.search.SearchFilterScreen
import com.project17.tourbooking.activities.search.SearchScreen
//import com.project17.tourbooking.activities.search.SearchScreen
import com.project17.tourbooking.activities.search.SearchViewModel
//import com.project17.tourbooking.activities.tripdetail.TripDetailScreen
import com.project17.tourbooking.utils.AuthViewModel
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
    appViewModel: AppViewModel = viewModel()
) {
    NavHost(navController, startDestination = NavigationItems.Home.route) {
        composable(NavigationItems.Home.route) {
            onBottomBarVisibilityChanged(true)
            HomeScreen(navController = navController, appViewModel = appViewModel)
        }

        composable(NavigationItems.MyTrip.route) {
            onBottomBarVisibilityChanged(true)
            MyTripActivity(SearchViewModel(), navController, AuthViewModel())
        }

        composable(NavigationItems.WishList.route) {
            onBottomBarVisibilityChanged(true)
            YourWishlist(SearchViewModel(), navController)
        }

        composable(NavigationItems.Profile.route) {
            onBottomBarVisibilityChanged(true)
            ProfileActivity(navController, AuthViewModel())
        }

//        composable(NavigationItems.TripDetail.route + "/{tourId}") { backStackEntry ->
//            onBottomBarVisibilityChanged(false)
//            val tourId = backStackEntry.arguments?.getString("tourId")
//            TripDetailScreen(navController, tourId ?: "")
//        }

        composable(NavigationItems.Search.route) {
            onBottomBarVisibilityChanged(false)
            SearchScreen(navController, appViewModel)
        }

        composable(NavigationItems.SearchFilter.route) {
            onBottomBarVisibilityChanged(false)
            SearchFilterScreen(navController, appViewModel)
        }
    }
}
