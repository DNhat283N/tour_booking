package com.project17.tourbooking.activities.user.authenticate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project17.tourbooking.ui.theme.Typography
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerification(navController: NavController){

    var otpValue by remember { mutableStateOf("") }
    var timeRemaining by remember { mutableStateOf(300) }
    var canResend by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = canResend) {
        if (!canResend) {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining--
            }
            canResend = true
        }
    }

    fun resendCode() {
        timeRemaining = 300
        canResend = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Create Your Account",
                style = Typography.headlineMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "OTP Verification",
                style = Typography.headlineLarge,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 0 until 4) {
                    OutlinedTextField(
                        value = otpValue.getOrNull(i)?.toString() ?: "",
                        onValueChange = {
                            if (it.length <= 1) {
                                otpValue = otpValue.take(i) + it + otpValue.drop(i + 1)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(60.dp)
                            .padding(4.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFFFCD240)
                    )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (canResend) {
                TextButton(onClick = { resendCode() }) {
                    Text("Send code again", style = Typography.bodyLarge, modifier = Modifier.padding(start = 16.dp))
                }
            } else {
                Text("Send code again in ${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                    style = Typography.bodyLarge, modifier = Modifier.padding(start = 16.dp))
            }


            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = { navController.navigate("account_created") },
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
                    "Submit",
                    style = Typography.headlineSmall,
                )
            }
        }
        IconButton(
            onClick =  { navController.navigate("create_account") },
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}