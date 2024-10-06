package com.project17.tourbooking.activities.user.authenticate

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.project17.tourbooking.R
import com.project17.tourbooking.navigates.NavigationItems
import com.project17.tourbooking.ui.theme.BlackDark900
import com.project17.tourbooking.ui.theme.BlackLight300
import com.project17.tourbooking.ui.theme.BrandDefault500
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.composables.PasswordOutlinedTextField
import com.project17.tourbooking.viewmodels.AuthState
import com.project17.tourbooking.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TourBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CreateAccountScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CreateAccountScreen(
    navController: NavController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val interactionSourcePassword = remember { MutableInteractionSource() }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val interactionSourceConfirmPassword = remember { MutableInteractionSource() }
    val interactionSourceFullName = remember { MutableInteractionSource() }
    var fullName by remember { mutableStateOf("") }
    val interactionSourceEmail = remember { MutableInteractionSource() }
    var email by remember { mutableStateOf("") }
    val interactionSourceUsername = remember { MutableInteractionSource() }
    var username by remember { mutableStateOf("") }
    val interactionSourcePhoneNumber = remember { MutableInteractionSource() }
    var phoneNumber by remember { mutableStateOf("") }
    val interactionSourceAddress = remember { MutableInteractionSource() }
    var address by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var dateOfBirth by remember { mutableStateOf<Date?>(null) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )

    val storage = FirebaseStorage.getInstance()
    val coroutineScope = rememberCoroutineScope()

    val authState = authViewModel.authState.observeAsState()
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(NavigationItems.Profile.route) {
                    popUpTo(NavigationItems.Login.route) { inclusive = true }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                Unit
            }
        }
    }

    fun handleCreateAccount() {
        coroutineScope.launch {
            if (password == confirmPassword) {
//                authViewModel.signUp(
//                    email,
//                    password,
//                    Customer(
//                        "",
//                        fullName,
//                        selectedGender,
//                        dateOfBirth?.let { Timestamp(it) } ?: Timestamp.now(),
//                        address,
//                        phoneNumber),
//                    Account("", username, "", ACCOUNT_ROLE.USER, 0, "", ACCOUNT_STATUS.ACTIVE)
//                )
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.password_does_not_match_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = stringResource(id = R.string.create_your_account_text),
                style = Typography.headlineMedium,
                color = BlackLight300,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.fill_in_the_information_below_text),
                style = Typography.headlineLarge,
                color = BlackDark900,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            CustomOutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = stringResource(id = R.string.full_name_text),
                interactionSource = interactionSourceFullName
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.date_of_birth_text) + ":",
                style = Typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
            Button(
                onClick = { showDatePicker(context){ dateOfBirth = it } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandDefault500,
                    contentColor = BlackDark900
                )
            ) {
                Text(formatDate(dateOfBirth, context))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = selectedGender,
                    onClick = { selectedGender = true },
                    colors = RadioButtonDefaults.colors(selectedColor = BrandDefault500)
                )
                Text(stringResource(id = R.string.male_text), style = Typography.bodyLarge)

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = !selectedGender,
                    onClick = { selectedGender = false },
                    colors = RadioButtonDefaults.colors(selectedColor = BrandDefault500)
                )
                Text(stringResource(id = R.string.female_text), style = Typography.bodyLarge)
            }

            CustomOutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = stringResource(id = R.string.address_text),
                interactionSource = interactionSourceAddress,
            )

            CustomOutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = stringResource(id = R.string.phone_number_text),
                interactionSource = interactionSourcePhoneNumber,
            )

            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(id = R.string.email_text),
                interactionSource = interactionSourceEmail,
            )

            CustomOutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = stringResource(id = R.string.user_name_text),
                interactionSource = interactionSourceUsername,
            )

            PasswordOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(id = R.string.password_text),
                isVisible = isPasswordVisible,
                onVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
                interactionSource = interactionSourcePassword
            )

            PasswordOutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = stringResource(id = R.string.confirm_password_text),
                isVisible = isConfirmPasswordVisible,
                onVisibilityToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                interactionSource = interactionSourceConfirmPassword
            )

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.password_rule_text),
                style = Typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.select_avatar_text),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = BlackDark900,
                modifier = Modifier.padding(start = 16.dp)
            )

            if (selectedImageUri != null) {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, selectedImageUri)
                } else {
                    val source =
                        ImageDecoder.createSource(context.contentResolver, selectedImageUri!!)
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

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.padding(start = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandDefault500,
                    contentColor = BlackDark900
                )
            ) {
                Text(
                    text = stringResource(id = R.string.pick_avatar_text),
                    style = Typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { handleCreateAccount() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandDefault500,
                    contentColor = BlackDark900
                )
            ) {
                Text(
                    stringResource(id = R.string.submit_button_text),
                    style = Typography.headlineSmall,
                )
            }
        }

        HeaderSection(navController = navController, modifier = Modifier.align(Alignment.TopStart))
    }
}

@Composable
fun HeaderSection(navController: NavController, modifier: Modifier) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = modifier
            .padding(start = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back"
        )
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = Typography.titleMedium,
                color = BlackLight300
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        interactionSource = interactionSource,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (interactionSource.collectIsFocusedAsState().value) BrandDefault500 else BlackLight300,
            unfocusedBorderColor = BlackLight300,
            focusedLabelColor = if (interactionSource.collectIsFocusedAsState().value) BrandDefault500 else BlackLight300,
            unfocusedLabelColor = BlackLight300
        ),
        singleLine = true
    )
}
private fun formatDate(date: Date?, context: Context): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return date?.let { format.format(it) }
        ?: context.getString(R.string.select_date_of_birth_text)
}

fun showDatePicker(context: Context, callback: (Date) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val dateOfBirth = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            callback(dateOfBirth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}