package com.project17.tourbooking.activities.user.authenticate
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project17.tourbooking.helper.FirestoreHelper
import com.project17.tourbooking.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    val interactionSourceEmail = remember { MutableInteractionSource() }
    val isFocusedEmail by interactionSourceEmail.collectIsFocusedAsState()
    val coroutineScope = rememberCoroutineScope()

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
                text = "Input your email",
                style = Typography.headlineMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Forgot password",
                style = Typography.headlineLarge,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", style = Typography.titleMedium, color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                interactionSource = interactionSourceEmail,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isFocusedEmail) Color(0xFFFCD240) else Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = if (isFocusedEmail) Color(0xFFFCD240) else Color.LightGray,
                    unfocusedLabelColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val emailExists = FirestoreHelper.isEmailExists(email)
                        if (emailExists) {
                            // Chuyển email sang màn hình đổi mật khẩu
                            navController.navigate("create_new_password/${email}")
                        } else {
                            emailError = "Email not found. Please check and try again."
                        }
                    }
                },
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
                Text("Submit", style = Typography.headlineSmall)
            }

            emailError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
            }
        }

        IconButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }
    }
}
