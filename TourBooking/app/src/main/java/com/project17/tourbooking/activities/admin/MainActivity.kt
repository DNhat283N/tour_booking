package com.project17.tourbooking.activities.admin

import ManageAccountsScreen
import MyTripActivity
import ProfileActivity
import YourWishlist
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project17.tourbooking.activities.admin.ui.theme.TourBookingTheme
import com.project17.tourbooking.activities.search.SearchViewModel
import com.project17.tourbooking.activities.user.AccountCreated
import com.project17.tourbooking.activities.user.CreateAccount
import com.project17.tourbooking.activities.user.CreateNewPassword
import com.project17.tourbooking.activities.user.Explore
import com.project17.tourbooking.activities.user.ForgotPasswordScreen
import com.project17.tourbooking.activities.user.LoginScreen
import com.project17.tourbooking.activities.user.OtpVerification

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourBookingTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "profile") {
                    composable("login") { LoginScreen(navController) }
                    composable("create_account") { CreateAccount(navController) }
                    composable("otp_verification") { OtpVerification(navController) }
                    composable("forgot_password") { ForgotPasswordScreen(navController) }
                    composable("create_new_password/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        CreateNewPassword(navController, email)
                    }
                    composable("account_created") { AccountCreated(navController) }
                    composable("explore") { Explore(navController) }
                    composable("my_trip") { MyTripActivity(SearchViewModel(), navController) }
                    composable("profile") { ProfileActivity(navController) }
                    composable("wishlist") { YourWishlist(SearchViewModel(), navController) }
                    composable("manage_categories") { ManageCategoriesScreen(navController) }
                    composable("manage_tours") { ManageToursScreen(navController) }
                    composable("edit_tour/{tourId}") { backStackEntry ->
                        val tourId = backStackEntry.arguments?.getString("tourId")
                        if (tourId != null) {
                            EditTourScreen(tourId, navController)
                        }
                    }
                    composable("add_tour") { AddTourScreen(navController) }
                    composable("manage_accounts") { ManageAccountsScreen(navController) }
                }
            }
        }
    }
}

