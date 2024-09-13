package com.project17.tourbooking.navigates

import BottomBar
import ManageAccountsScreen
import MyTripScreen
import ProfileScreen
import WishListScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project17.tourbooking.activities.admin.tours.AddTourScreen
import com.project17.tourbooking.activities.admin.tours.EditTourScreen
import com.project17.tourbooking.activities.admin.categories.ManageCategoriesScreen
import com.project17.tourbooking.activities.admin.ManageOverviewScreen
import com.project17.tourbooking.activities.admin.tours.ManageToursScreen
import com.project17.tourbooking.activities.admin.statistics.OrderStatisticsScreen
import com.project17.tourbooking.activities.user.home.HomeScreen
import com.project17.tourbooking.activities.user.pay.BookingDetailScreen
import com.project17.tourbooking.activities.user.pay.BookingSuccessScreen
import com.project17.tourbooking.activities.user.search.SearchFilterScreen
import com.project17.tourbooking.activities.user.search.SearchScreen
import com.project17.tourbooking.activities.user.search.SearchViewModel
import com.project17.tourbooking.activities.user.tripdetail.BillDetailScreen
import com.project17.tourbooking.activities.user.tripdetail.TripDetailScreen
import com.project17.tourbooking.activities.user.authenticate.CreateAccount
import com.project17.tourbooking.activities.user.authenticate.Explore
import com.project17.tourbooking.activities.user.authenticate.ForgotPasswordScreen
import com.project17.tourbooking.activities.user.authenticate.LoginScreen
import com.project17.tourbooking.viewmodels.AuthViewModel
import com.project17.tourbooking.viewmodels.AppViewModel

@Composable
fun VisibilityBottomBarScaffold(
    navController: NavHostController,
    isBottomBarVisible: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BottomBar(navController = navController)
            } else {
            }
        }
    ) {innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController, onBottomBarVisibilityChanged: (Boolean) -> Unit, appViewModel: AppViewModel = viewModel()){
    NavHost(navController, startDestination = NavigationItems.Home.route, builder = {
        composable(NavigationItems.Home.route){
            onBottomBarVisibilityChanged(true)
            HomeScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(NavigationItems.MyTrip.route){
            onBottomBarVisibilityChanged(true)
            MyTripScreen(navController = navController, authViewModel = AuthViewModel(), searchViewModel = SearchViewModel())
        }
        composable(NavigationItems.WishList.route){
            onBottomBarVisibilityChanged(true)
            WishListScreen(navController = navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.Profile.route){
            onBottomBarVisibilityChanged(true)
            ProfileScreen(navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.TripDetail.route + "/{tourId}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType}
        )){
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            TripDetailScreen(navController, tourId ?: "")
        }
        composable(NavigationItems.TripBookedDetail.route + "/{billId}", arguments = listOf(
            navArgument("billId"){type = NavType.StringType}
        )){
            onBottomBarVisibilityChanged(false)
            val billId = it.arguments?.getString("billId")
            BillDetailScreen(billId ?: "")
        }
        composable(NavigationItems.Login.route){
            onBottomBarVisibilityChanged(false)
            LoginScreen(navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.Register.route){
            onBottomBarVisibilityChanged(false)
            CreateAccount(navController = navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.ForgotPassword.route){
            onBottomBarVisibilityChanged(false)
            ForgotPasswordScreen(navController = navController)
        }
        composable(NavigationItems.AccountCreated.route) {
            onBottomBarVisibilityChanged(false)
            CreateAccount(navController = navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.ManageTour.route) {
            onBottomBarVisibilityChanged(false)
            ManageToursScreen(navController = navController)
        }
        composable(NavigationItems.ManageAccount.route) {
            onBottomBarVisibilityChanged(false)
            ManageAccountsScreen(navController = navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.ManageCategory.route) {
            onBottomBarVisibilityChanged(false)
            ManageCategoriesScreen(navController = navController)
        }
        composable(NavigationItems.AddTour.route) {
            onBottomBarVisibilityChanged(false)
            AddTourScreen(navController = navController)
        }
        composable(NavigationItems.Explore.route) {
            onBottomBarVisibilityChanged(false)
            Explore(navController = navController)
        }
        composable(NavigationItems.EditTour.route + "/{tourId}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType}
        )) {
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            EditTourScreen(tourId = tourId ?: "", navController = navController)
        }
        composable(NavigationItems.ManageOverview.route) {
            onBottomBarVisibilityChanged(false)
            ManageOverviewScreen(navController = navController, authViewModel = AuthViewModel())
        }
        composable(NavigationItems.Statistic.route) {
            onBottomBarVisibilityChanged(false)
            OrderStatisticsScreen()
        }
        composable(NavigationItems.Search.route){
            onBottomBarVisibilityChanged(false)
            SearchScreen(navController, appViewModel)
        }
        composable(NavigationItems.SearchFilter.route){
            onBottomBarVisibilityChanged(false)
            SearchFilterScreen(navController, appViewModel)
        }
        composable(NavigationItems.BookingSuccess.route + "/{billId}", arguments = listOf(
            navArgument("billId"){type = NavType.StringType}
        )) {
            onBottomBarVisibilityChanged(false)
            val billId = it.arguments?.getString("billId")
            BookingSuccessScreen(billId = billId ?: "", authViewModel = AuthViewModel())
        }
        composable(NavigationItems.BookingDetail.route + "/{tourId}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType}
        )){
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            BookingDetailScreen(navController, tourId ?: "")
        }
        composable(NavigationItems.BookingPaymentMethod.route + "/{tourId}/{quantity}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType},
            navArgument("quantity"){type = NavType.IntType}
        )){
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            BookingDetailScreen(navController, tourId ?: "")
        }
    })
}