package com.project17.tourbooking.activities.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project17.tourbooking.activities.admin.ui.theme.TourBookingTheme
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
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("create_account") { CreateAccount(navController) }
                    composable("forgot_password") { ForgotPasswordScreen(navController) }
                    composable("create_new_password") { CreateNewPassword(navController) }
                    composable("account_created") { AccountCreated(navController) }
                    composable("explore") { Explore(navController) }
                    composable("otp_verification") { OtpVerification(navController) }
                }
            }
        }
    }
}

