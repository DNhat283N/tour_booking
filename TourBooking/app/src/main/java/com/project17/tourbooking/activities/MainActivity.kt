package com.project17.tourbooking.activities

import BottomBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.project17.tourbooking.navigates.NavigationGraph
import com.project17.tourbooking.navigates.VisibilityBottomBarScaffold
import com.project17.tourbooking.ui.theme.TourBookingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourBookingTheme {
                val navController = rememberNavController()
                var isBottomBarVisible by remember{
                    mutableStateOf(true)
                }
               VisibilityBottomBarScaffold(
                   navController = navController,
                   isBottomBarVisible = isBottomBarVisible
               ) {
                   Column(modifier = Modifier
                       .fillMaxSize()) {
                       NavigationGraph(
                           navController = navController,
                           onBottomBarVisibilityChanged = {
                               visible ->
                               isBottomBarVisible = visible
                           }
                       )
                   }
               }
            }
        }
    }
}
