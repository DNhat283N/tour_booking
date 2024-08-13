package com.project17.tourbooking.activities.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project17.tourbooking.R
import com.project17.tourbooking.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val interactionSourceEmail = remember { MutableInteractionSource() }
    val isFocusedEmail by interactionSourceEmail.collectIsFocusedAsState()
    val interactionSourcePassword = remember { MutableInteractionSource() }
    val isFocusedPassword by interactionSourcePassword.collectIsFocusedAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo ứng dụng du lịch
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "Travel App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        // Email hoặc Username
        OutlinedTextField(
            value = emailOrUsername,
            onValueChange = { emailOrUsername = it },
            label = { Text("Email or Username", style = Typography.titleMedium, color = Color.LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            interactionSource = interactionSourceEmail,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isFocusedEmail) Color(0xFFFCD240) else Color.LightGray, // Màu viền khi focus
                unfocusedBorderColor = Color.LightGray, // Màu viền khi không focus
                focusedLabelColor = if (isFocusedEmail) Color(0xFFFCD240) else Color.LightGray, // Màu label khi focus
                unfocusedLabelColor = Color.LightGray // Màu label khi không focus
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password", style = Typography.titleMedium, color = Color.LightGray) },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            interactionSource = interactionSourcePassword,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isFocusedPassword) Color(0xFFFCD240) else Color.LightGray, // Màu viền khi focus
                unfocusedBorderColor = Color.LightGray, // Màu viền khi không focus
                focusedLabelColor = if (isFocusedPassword) Color(0xFFFCD240) else Color.LightGray, // Màu label khi focus
                unfocusedLabelColor = Color.LightGray // Màu label khi không focus
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text(
                    text = "Remember me",
                    style = Typography.bodyLarge,
                    color = Color.LightGray
                )
            }

            TextButton(onClick = { navController.navigate("forgot_password") }) {
                Text(
                    text = "Forgot password?",
                    style = Typography.bodyLarge,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút đăng nhập
        Button(
            onClick = { /* Xử lý đăng nhập */ },
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
                "Sign In",
                style = Typography.headlineSmall,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("create_account") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            )
        ) {
            Text(
                "Create Account",
                style = Typography.headlineSmall,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Đăng nhập bằng mạng xã hội
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "Facebook Login",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* Xử lý đăng nhập bằng Facebook */ }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_instagram),
                contentDescription = "Instagram Login",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* Xử lý đăng nhập bằng Instagram */ }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Login",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* Xử lý đăng nhập bằng Google */ }
            )
        }
    }
}
