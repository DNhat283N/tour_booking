package com.project17.tourbooking.activities.user.authenticate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project17.tourbooking.R
import com.project17.tourbooking.ui.theme.Typography

@Composable
fun AccountCreated (navController: NavController){


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_location_2),
                contentDescription = "Successful Create Account",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
            )
        }


        Text(
            text = "Successful created an account",
            style = Typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = "After this you can explore any place you want to enjoy it!",
            style = Typography.titleLarge,
            color = Color.LightGray,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(250.dp))

        Button(
            onClick = { navController.navigate("explore") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFCD240),
                contentColor = Color.Black
            )
        ) {
            Text(
                "Let's Explore!",
                style = Typography.headlineSmall,
            )
        }
    }
}