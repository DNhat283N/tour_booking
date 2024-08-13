package com.project17.tourbooking.activities.user

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project17.tourbooking.ui.theme.Typography
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPassword(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val interactionSourcePassword = remember { MutableInteractionSource() }
    val isFocusedPassword by interactionSourcePassword.collectIsFocusedAsState()
    val icon = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    val interactionSourceConfirmPassword = remember { MutableInteractionSource() }
    val isFocusedConfirmPassword by interactionSourceConfirmPassword.collectIsFocusedAsState()
    val icon1 = if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

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
                text = "Forgot Password",
                style = Typography.headlineMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create New Password",
                style = Typography.headlineLarge,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Confirm Password", style = Typography.titleMedium, color = Color.LightGray) },
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                interactionSource = interactionSourceConfirmPassword,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isFocusedConfirmPassword) Color(0xFFFCD240) else Color.LightGray, // Màu viền khi focus
                    unfocusedBorderColor = Color.LightGray, // Màu viền khi không focus
                    focusedLabelColor = if (isFocusedConfirmPassword) Color(0xFFFCD240) else Color.LightGray, // Màu label khi focus
                    unfocusedLabelColor = Color.LightGray // Màu label khi không focus
                ),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                        Icon(imageVector = icon1, contentDescription = "Toggle password visibility")
                    }
                }
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Your password must include at least one symbol and be 8 or more characters long",
                style = Typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(260.dp))

            Button(
                onClick = { navController.navigate("login") },
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
                    "Save",
                    style = Typography.headlineSmall,
                )
            }
        }
        IconButton(
            onClick =  { navController.navigate("forgot_password") },
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