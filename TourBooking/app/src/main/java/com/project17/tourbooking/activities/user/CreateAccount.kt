package com.project17.tourbooking.activities.user

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project17.tourbooking.ui.theme.Typography
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccount(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val interactionSourcePassword = remember { MutableInteractionSource() }
    val isFocusedPassword by interactionSourcePassword.collectIsFocusedAsState()
    val icon = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    val interactionSourceConfirmPassword = remember { MutableInteractionSource() }
    val isFocusedConfirmPassword by interactionSourceConfirmPassword.collectIsFocusedAsState()
    val icon1 =
        if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val interactionSourceFullName = remember { MutableInteractionSource() }
    val isFocusedFullName by interactionSourceFullName.collectIsFocusedAsState()
    var fullName by remember { mutableStateOf("") }
    val interactionSourceEmail = remember { MutableInteractionSource() }
    val isFocusedEmail by interactionSourceEmail.collectIsFocusedAsState()
    var email by remember { mutableStateOf("") }
    val interactionSourceUsername = remember { MutableInteractionSource() }
    val isFocusedUsername by interactionSourceFullName.collectIsFocusedAsState()
    var username by remember { mutableStateOf("") }
    val interactionSourcePhoneNumber = remember { MutableInteractionSource() }
    val isFocusedPhoneNumber by interactionSourceFullName.collectIsFocusedAsState()
    var phoneNumber by remember { mutableStateOf("") }
    val interactionSourceAddress = remember { MutableInteractionSource() }
    val isFocusedAddress by interactionSourceFullName.collectIsFocusedAsState()
    var address by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(true) }
    val calendar = Calendar.getInstance()
    val context = LocalContext.current
    var dateOfBirth by remember { mutableStateOf("Select Date of Birth") }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )


    MaterialTheme{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Create Your Account",
                    style = Typography.headlineMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                ) }

            item { Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Fill in the information below",
                    style = Typography.headlineLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                ) }

            item { Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = {
                        Text(
                            "Fullname",
                            style = Typography.titleMedium,
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    interactionSource = interactionSourceFullName,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (isFocusedFullName) Color(0xFFFCD240) else Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = if (isFocusedFullName) Color(0xFFFCD240) else Color.LightGray,
                        unfocusedLabelColor = Color.LightGray
                    )
                ) }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Date of Birth:",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Button(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(dateOfBirth)
                }
            }

            item { Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = selectedGender,
                    onClick = { selectedGender = true }
                )
                Text("Male")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = !selectedGender,
                    onClick = { selectedGender = false }
                )
                Text("Female", style = Typography.bodyLarge)
            } }



            item { OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = {
                    Text(
                        "Address",
                        style = Typography.titleMedium,
                        color = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                interactionSource = interactionSourceAddress,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isFocusedAddress) Color(0xFFFCD240) else Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = if (isFocusedAddress) Color(0xFFFCD240) else Color.LightGray,
                    unfocusedLabelColor = Color.LightGray
                )
            )  }



            item { OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = {
                    Text(
                        "Phone Number",
                        style = Typography.titleMedium,
                        color = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                interactionSource = interactionSourcePhoneNumber,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isFocusedPhoneNumber) Color(0xFFFCD240) else Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = if (isFocusedPhoneNumber) Color(0xFFFCD240) else Color.LightGray,
                    unfocusedLabelColor = Color.LightGray
                )
            ) }

            item { OutlinedTextField(
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
                    focusedLabelColor = if (isFocusedEmail) Color(0xFFFCD240) else Color.LightGray, // Màu label khi focus
                    unfocusedLabelColor = Color.LightGray
                )
            ) }


            item { OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = {
                    Text(
                        "Username",
                        style = Typography.titleMedium,
                        color = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                interactionSource = interactionSourceUsername,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isFocusedUsername) Color(0xFFFCD240) else Color.LightGray, // Màu viền khi focus
                    unfocusedBorderColor = Color.LightGray, // Màu viền khi không focus
                    focusedLabelColor = if (isFocusedUsername) Color(0xFFFCD240) else Color.LightGray, // Màu label khi focus
                    unfocusedLabelColor = Color.LightGray // Màu label khi không focus
                )
            ) }


            item { OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        text = "Password",
                        style = Typography.titleMedium,
                        color = Color.LightGray
                    )
                },
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
            ) }

            item { OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(
                        text
                        = "Confirm Password",
                        style = Typography.titleMedium,
                        color = Color.LightGray
                    )
                },
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
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(imageVector = icon1, contentDescription = "Toggle password visibility")
                    }
                }
            ) }

            item { Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Your password must include at least one symbol and be 8 or more characters long",
                    style = Typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                ) }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Select Avatar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )

                if (selectedImageUri != null) {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, selectedImageUri)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, selectedImageUri!!)
                        ImageDecoder.decodeBitmap(source)
                    }

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Selected Avatar",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(model = "https://via.placeholder.com/150"),
                        contentDescription = "Default Avatar",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = "Pick Avatar", style = Typography.titleLarge)
                    Modifier.padding(16.dp)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("otp_verification") },
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
                } }

        }
        IconButton(
            onClick = { navController.navigate("login") },
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
}


