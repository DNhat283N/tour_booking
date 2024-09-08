package com.project17.tourbooking.activities.pay

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project17.tourbooking.R
import com.project17.tourbooking.R.drawable.fuji_mountain
import com.project17.tourbooking.navigates.NavigationItems
import com.project17.tourbooking.ui.theme.BlackDark900
import com.project17.tourbooking.ui.theme.BlackLight200
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.BrandDefault500
import com.project17.tourbooking.ui.theme.ErrorDefault500
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.Tour

class PayDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var paymentStatus by remember { mutableStateOf("") }
            var token by remember { mutableStateOf("") }
            var isPayButtonEnabled by remember { mutableStateOf(false) }
            TourBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    BookingDetailScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BookingDetailScreen(navController: NavHostController = rememberNavController(), tourId: String = "", modifier: Modifier = Modifier){
    var tour = Tour("Fuji Mountain", fuji_mountain, 4.5, "Japan", 245.0, true)
    var customerName by remember { mutableStateOf("Pristina") }
    var contactInfo by remember { mutableStateOf("example@gmail.com") }
    var quantity by remember { mutableIntStateOf(1) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier
                .height(16.dp)
                .fillMaxWidth())

            NavBarSection(navController)
            Spacer(modifier = Modifier
                .height(32.dp)
                .fillMaxWidth())
            BookingForm(
                customerName = customerName,
                contactInfo = contactInfo,
                onCustomerNameChanged = { customerName = it },
                onContactInfoChanged = { contactInfo = it },
                onQuantityChanged = { quantity = it + 1 }
            )
        }
        FooterSection(
            tour = tour,
            quantity = quantity,
            onConfirmClick = {
                navController.navigate(NavigationItems.PayActivity.route + "/${quantity}")
               // navController.navigate(NavigationItems.BookingPaymentMethod.route + "/${tourId}/${quantity}/${customerName}/${contactInfo}")
            },
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
fun NavBarSection(
    navController: NavHostController = rememberNavController()
){
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "",
            tint = BlackDark900,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable { navController.popBackStack() }
        )
        Text(
            text = stringResource(id = R.string.title_activity_detail_booking_text),
            style = Typography.titleLarge,
            color = BlackDark900,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BookingForm(
    customerName: String,
    contactInfo: String,
    onCustomerNameChanged: (String) -> Unit,
    onContactInfoChanged: (String) -> Unit,
    onQuantityChanged: (Int) -> Unit
){
    //TODO: load customer's name from account
    var showErrorName by remember { mutableStateOf(false) }
    var showErrorContact by remember { mutableStateOf(false) }
    var errorMessageName by remember { mutableStateOf<String?>(null) }
    var errorMessageContact by remember { mutableStateOf<String?>(null) }

    Column {
        InformationTextField(
            label = R.string.person_responsible_text,
            value = customerName,
            onValueChange = { newValue ->
                onCustomerNameChanged(newValue)
                if (showErrorName) {
                    showErrorName = false
                }
            },
            errorMessage = errorMessageName,
            showError = showErrorName
        )

        Spacer(modifier = Modifier.height(16.dp))
        //TODO: load customer's email from account
        InformationTextField(
            label = R.string.contact_info_text,
            value = contactInfo,
            onValueChange = { newValue ->
                onContactInfoChanged(newValue)
                if (showErrorContact) {
                    showErrorContact = false
                }
            },
            errorMessage = errorMessageContact,
            showError = showErrorContact,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        //TODO: get maxium slot can book and put in maxMembers
        val maxMembers = 10
        val dropdownList = (1..maxMembers).map { "$it " + stringResource(id = R.string.member_text) }
        DropdownMenuWithOptions(
            options = dropdownList,
            onOptionSelected = {newValue -> onQuantityChanged(newValue)}
        )
    }
}

@Composable
fun InformationTextField(
    label: Int,
    value: String = "",
    onValueChange: (String) -> Unit,
    errorMessage: String? = null,
    showError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        OutlinedTextField(
            value = value,
            readOnly = true,
            onValueChange = { newValue -> onValueChange(newValue) },
            label = { Text(stringResource(id = label)) },
            singleLine = true,
            isError = showError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (showError) Color.Red else BlackLight200,
                focusedBorderColor = if (showError) Color.Red else BlackDark900,
                focusedLabelColor = if (showError) Color.Red else BlackDark900
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            )
        )
        if (showError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = Typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithOptions(options: List<String>,  onOptionSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            singleLine = true,
            label = { Text(stringResource(id = R.string.member_text)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.image_description_text),
                    Modifier.clickable { expanded = expanded }
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BlackLight200,
                focusedBorderColor = BlackDark900,
                focusedLabelColor = BlackDark900
            ),
            shape = RoundedCornerShape(16.dp),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(BlackWhite0)
        ) {
            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onOptionSelected(index)
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun FooterSection(
    tour: Tour,
    quantity: Int,
    onConfirmClick: () -> Unit,
    modifier: Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BlackWhite0),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = stringResource(id = R.string.total_text),
                    color = BlackLight400,
                    style = Typography.bodyLarge)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = String.format("$%.2f", tour.price * quantity),
                    style = Typography.headlineMedium,
                    color = ErrorDefault500)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onConfirmClick() },
                colors = ButtonColors(BrandDefault500, BlackDark900, BrandDefault500, BlackDark900),
                modifier = Modifier
                    .width(150.dp)
            ){
                Text(
                    text = stringResource(id = R.string.confirm_text),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    TourBookingTheme {
        BookingDetailScreen()
    }
}