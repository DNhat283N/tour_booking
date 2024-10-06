package com.project17.tourbooking.navigates

import BottomBar
import ManageAccountsScreen
import MyTripScreen
import WishListScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project17.tourbooking.activities.admin.categories.ManageCategoriesScreen
import com.project17.tourbooking.activities.admin.coin_packages.ManageCoinPackageScreen
import com.project17.tourbooking.activities.admin.destination.ManageDestinationScreen
import com.project17.tourbooking.activities.admin.home.ManageOverviewScreen
import com.project17.tourbooking.activities.admin.statistics.OrderStatisticsScreen
import com.project17.tourbooking.activities.admin.tours.ManageToursScreen
import com.project17.tourbooking.activities.admin.tours.addtour.AddTourScreen
import com.project17.tourbooking.activities.admin.tours.edittour.EditTourScreen
import com.project17.tourbooking.activities.user.authenticate.CreateAccountScreen
import com.project17.tourbooking.activities.user.authenticate.createaccount.CreatePasswordScreen
import com.project17.tourbooking.activities.user.authenticate.createaccount.EmailInputScreen
import com.project17.tourbooking.activities.user.authenticate.createaccount.NameInputScreen
import com.project17.tourbooking.activities.user.authenticate.forgotpassword.ForgotPasswordScreen
import com.project17.tourbooking.activities.user.authenticate.login.LoginScreen
import com.project17.tourbooking.activities.user.home.HomeScreen
import com.project17.tourbooking.activities.user.pay.presentation.bookingsuccess.BookingSuccessScreen
import com.project17.tourbooking.activities.user.pay.presentation.coinbooking.CoinPackageChoosingScreen
import com.project17.tourbooking.activities.user.pay.presentation.paymentmethod.PaymentMethodScreen
import com.project17.tourbooking.activities.user.pay.presentation.tourbooking.BookingDetailScreen
import com.project17.tourbooking.activities.user.pay.viewmodel.PayViewModel
import com.project17.tourbooking.activities.user.profile.ProfileScreen
import com.project17.tourbooking.activities.user.search.SearchFilterScreen
import com.project17.tourbooking.activities.user.search.SearchScreen
import com.project17.tourbooking.activities.user.search.viewmodel.SearchViewModel
import com.project17.tourbooking.activities.user.tripdetail.BillDetailScreen
import com.project17.tourbooking.activities.user.tripdetail.TripDetailScreen
import com.project17.tourbooking.constant.ACCOUNT_ROLE
import com.project17.tourbooking.helper.firestore_helper.FirestoreHelper
import com.project17.tourbooking.viewmodels.AppViewModel
import com.project17.tourbooking.viewmodels.AuthState
import com.project17.tourbooking.viewmodels.AuthViewModel

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
            }
        }
    ) {innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
    appViewModel: AppViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    payViewModel: PayViewModel = viewModel()
){
    var startDestination by remember {
        mutableStateOf(NavigationItems.Home.route)
    }
    if(authViewModel.authState.value is AuthState.Authenticated){
        LaunchedEffect(authViewModel.authState.value) {
            val userRole = FirestoreHelper.getUserRole(authViewModel.getCurrentUser()?.uid!!)
            startDestination = if(userRole == ACCOUNT_ROLE.ADMIN.toString() || userRole == ACCOUNT_ROLE.STAFF.toString()){
                NavigationItems.ManageOverview.route
            } else NavigationItems.Home.route
        }
    }
    else startDestination = NavigationItems.Home.route

    NavHost(navController, startDestination = startDestination, builder = {
        // Four items in bottom bar
        composable(NavigationItems.Home.route){
            onBottomBarVisibilityChanged(true)
            HomeScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(NavigationItems.MyTrip.route){
            onBottomBarVisibilityChanged(true)
            MyTripScreen(navController = navController, authViewModel = authViewModel, searchViewModel = searchViewModel)
        }
        composable(NavigationItems.WishList.route){
            onBottomBarVisibilityChanged(true)
            WishListScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(NavigationItems.Profile.route){
            onBottomBarVisibilityChanged(true)
            ProfileScreen(navController, authViewModel = authViewModel)
        }

        // Items for client
        composable(NavigationItems.TripDetail.route + "/{tourId}/{location}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType},
            navArgument("location"){type = NavType.StringType}
        )){
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            val location = it.arguments?.getString("location")
            TripDetailScreen(navController, tourId ?: "", location ?: "")
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
            LoginScreen(navController, authViewModel = authViewModel)
        }
        composable(NavigationItems.InputFullName.route){
            onBottomBarVisibilityChanged(false)
            NameInputScreen(navController = navController)
        }
        composable(NavigationItems.InputEmail.route + "/{fullName}/{userName}", arguments = listOf(
            navArgument("fullName"){type = NavType.StringType},
            navArgument("userName"){type = NavType.StringType }
        )){
            val fullName = it.arguments?.getString("fullName")
            val userName = it.arguments?.getString("userName")
            onBottomBarVisibilityChanged(false)
            EmailInputScreen(navController = navController, fullName = fullName ?: "", userName = userName ?: "")
        }
        composable(NavigationItems.CreatePassword.route + "/{fullName}/{userName}/{email}", arguments = listOf(
            navArgument("fullName"){type = NavType.StringType},
            navArgument("userName"){type = NavType.StringType },
            navArgument("email"){type = NavType.StringType }
        )){
            val fullName = it.arguments?.getString("fullName") ?: ""
            val userName = it.arguments?.getString("userName") ?: ""
            val email = it.arguments?.getString("email") ?: ""
            onBottomBarVisibilityChanged(false)
            CreatePasswordScreen(navController, fullName, userName, email, authViewModel)
        }
        composable(NavigationItems.AccountCreated.route){
            onBottomBarVisibilityChanged(false)
            CreateAccountScreen(navController = navController, authViewModel)
        }
        composable(NavigationItems.ForgotPassword.route){
            onBottomBarVisibilityChanged(false)
            ForgotPasswordScreen(navController = navController)
        }
        composable(NavigationItems.AccountCreated.route) {
            onBottomBarVisibilityChanged(false)
            CreateAccountScreen(navController = navController, authViewModel)
        }

        composable(NavigationItems.Search.route){
            onBottomBarVisibilityChanged(false)
            SearchScreen(navController, appViewModel, searchViewModel)
        }
        composable(NavigationItems.Search.route + "/{categoryId}", arguments = listOf(
            navArgument("categoryId"){type = NavType.StringType}
        )){
            onBottomBarVisibilityChanged(false)
            val categoryId = it.arguments?.getString("categoryId") ?: ""
            SearchScreen(navController, appViewModel, searchViewModel, categoryId)
        }
        composable(NavigationItems.SearchFilter.route){
            onBottomBarVisibilityChanged(false)
            SearchFilterScreen(navController, appViewModel)
        }

        composable(NavigationItems.ManageOverview.route) {
            onBottomBarVisibilityChanged(false)
            ManageOverviewScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(NavigationItems.ManageTour.route) {
            onBottomBarVisibilityChanged(false)
            ManageToursScreen(navController = navController)
        }
        composable(NavigationItems.AddTour.route) {
            onBottomBarVisibilityChanged(false)
            AddTourScreen(navController = navController)
        }
        composable(NavigationItems.EditTour.route + "/{tourId}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType}
        )) {
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            EditTourScreen(tourId = tourId ?: "", navController = navController)
        }

        composable(NavigationItems.ManageAccount.route) {
            onBottomBarVisibilityChanged(false)
            ManageAccountsScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(NavigationItems.ManageCategory.route) {
            onBottomBarVisibilityChanged(false)
            ManageCategoriesScreen(navController = navController)
        }
        composable(NavigationItems.Statistic.route) {
            onBottomBarVisibilityChanged(false)
            OrderStatisticsScreen()
        }
        composable(NavigationItems.ManageCoinPackage.route){
            onBottomBarVisibilityChanged(false)
            ManageCoinPackageScreen(navController = navController)
        }
        composable(NavigationItems.ManageDestination.route){
            onBottomBarVisibilityChanged(false)
            ManageDestinationScreen(navController = navController)
        }

        composable(NavigationItems.BookingSuccess.route + "/{billId}", arguments = listOf(
            navArgument("billId"){type = NavType.StringType}
        )) {
            onBottomBarVisibilityChanged(false)
            val billId = it.arguments?.getString("billId")
            BookingSuccessScreen(billId = billId ?: "", authViewModel = authViewModel)
        }
        composable(NavigationItems.BookingDetail.route + "/{tourId}", arguments = listOf(
            navArgument("tourId"){type = NavType.StringType}
        )){
            onBottomBarVisibilityChanged(false)
            val tourId = it.arguments?.getString("tourId")
            BookingDetailScreen(navController, tourId ?: "", payViewModel = payViewModel)
        }
        composable(NavigationItems.CoinPackageBooking.route){
            onBottomBarVisibilityChanged(false)
            CoinPackageChoosingScreen(navController = navController, payViewModel = payViewModel)
        }
        composable(NavigationItems.PaymentMethod.route){
            onBottomBarVisibilityChanged(false)
            PaymentMethodScreen(navController = navController, payViewModel = payViewModel)
        }
    })
}