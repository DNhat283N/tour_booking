package com.project17.tourbooking.activities.admin

import FirestoreHelper
import android.app.DatePickerDialog
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.project17.tourbooking.activities.admin.ui.theme.TourBookingTheme
import com.project17.tourbooking.models.Tour
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.project17.tourbooking.models.Category

@Composable
fun ManageToursScreen(navController: NavController) {
    val (tours, setTours) = remember { mutableStateOf<List<Pair<String, Tour>>>(emptyList()) }
    val (categories, setCategories) = remember { mutableStateOf<List<Pair<String, Category>>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Tải danh sách tours và categories
        coroutineScope.launch {
            val fetchedTours = FirestoreHelper.getTours()
            val fetchedCategories = FirestoreHelper.getCategories()
            setTours(fetchedTours)
            setCategories(fetchedCategories)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            navController.navigate("add_tour")
        }) {
            Text("Add New Tour")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tours) { (id, tour) ->
                TourItem(
                    tour = tour,
                    categories = categories, // Truyền danh sách categories
                    onEdit = {
                        navController.navigate("edit_tour/$id")
                    },
                    onDelete = {
                        coroutineScope.launch {
                            FirestoreHelper.deleteTour(id)
                            val updatedTours = FirestoreHelper.getTours()
                            setTours(updatedTours)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun TourItem(
    tour: Tour,
    categories: List<Pair<String, Category>>, // Thêm tham số categories
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // Tìm tên category từ ID
    val categoryName = categories.find { it.first == tour.categoryId }?.second?.name ?: "Unknown"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            .clickable(onClick = onEdit)
    ) {
        Text(text = tour.name, style = MaterialTheme.typography.titleMedium)
        Text(text = "Category: $categoryName", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTourScreen(tourId: String, navController: NavController) {
    var tour by remember { mutableStateOf<Tour?>(null) }
    var name by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var slotQuantity by remember { mutableStateOf(0) }
    var image by remember { mutableStateOf("") }
    var openRegistrationDate by remember { mutableStateOf(Date()) }
    var closeRegistrationDate by remember { mutableStateOf(Date()) }
    var cancellationDeadline by remember { mutableStateOf(Date()) }
    var startDate by remember { mutableStateOf(Date()) }
    var averageRating by remember { mutableStateOf(0.0) }
    var categoryId by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf<List<Pair<String, Category>>>(emptyList()) }

    val context = LocalContext.current
    val activityResultRegistry = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val getImage = activityResultRegistry?.register("key", ActivityResultContracts.GetContent()) { uri ->
        uri?.let { image = uri.toString() }
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(tourId) {
        tour = FirestoreHelper.getTourById(tourId)
        categories = FirestoreHelper.getCategories()

        // Populate fields with existing tour data
        tour?.let {
            name = it.name
            destination = it.destination
            slotQuantity = it.slotQuantity
            image = it.image
            openRegistrationDate = it.openRegistrationDate.toDate()
            closeRegistrationDate = it.closeRegistrationDate.toDate()
            cancellationDeadline = it.cancellationDeadline.toDate()
            startDate = it.startDate.toDate()
            averageRating = it.averageRating
            categoryId = it.categoryId
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Edit Tour", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = slotQuantity.toString(),
            onValueChange = { slotQuantity = it.toIntOrNull() ?: 0 },
            label = { Text("Slot Quantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { getImage?.launch("image/*") }) {
            Text("Pick Image")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (image.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(image),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Open Registration Date",
            date = openRegistrationDate,
            onDateChanged = { newDate -> openRegistrationDate = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Close Registration Date",
            date = closeRegistrationDate,
            onDateChanged = { newDate -> closeRegistrationDate = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Cancellation Deadline",
            date = cancellationDeadline,
            onDateChanged = { newDate -> cancellationDeadline = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Start Date",
            date = startDate,
            onDateChanged = { newDate -> startDate = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = averageRating.toString(),
            onValueChange = { averageRating = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Average Rating") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Category Dropdown
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = categories.find { it.first == categoryId }?.second?.name ?: "Select Category",
                onValueChange = { },
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { (id, category) ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            categoryId = id
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Buttons for saving and canceling
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                // Navigate back without saving changes
                navController.navigateUp()
            }) {
                Text("Cancel")
            }

            Button(onClick = {
                if (name.isNotBlank() && destination.isNotBlank()) {
                    coroutineScope.launch {
                        FirestoreHelper.updateTour(
                            tourId,
                            Tour(
                                name = name,
                                openRegistrationDate = Timestamp(openRegistrationDate),
                                closeRegistrationDate = Timestamp(closeRegistrationDate),
                                cancellationDeadline = Timestamp(cancellationDeadline),
                                startDate = Timestamp(startDate),
                                destination = destination,
                                slotQuantity = slotQuantity,
                                image = image,
                                averageRating = averageRating,
                                categoryId = categoryId
                            )
                        )
                        navController.navigate("manage_tours")
                    }
                }
            }) {
                Text("Save")
            }
        }
    }
}
@Composable
fun DatePicker(title: String, date: Date, onDateChanged: (Date) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { time = date }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            onDateChanged(newDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedButton(onClick = { datePickerDialog.show() }) {
        Text("$title: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTourScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var slotQuantity by remember { mutableStateOf(0) }
    var image by remember { mutableStateOf("") }
    var openRegistrationDate by remember { mutableStateOf(Date()) }
    var closeRegistrationDate by remember { mutableStateOf(Date()) }
    var cancellationDeadline by remember { mutableStateOf(Date()) }
    var startDate by remember { mutableStateOf(Date()) }
    var averageRating by remember { mutableStateOf(0.0) }
    var categoryId by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf<List<Pair<String, Category>>>(emptyList()) }

    val context = LocalContext.current
    val activityResultRegistry = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val getImage = activityResultRegistry?.register("key", ActivityResultContracts.GetContent()) { uri ->
        uri?.let { image = uri.toString() }
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        categories = FirestoreHelper.getCategories()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add New Tour", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = slotQuantity.toString(),
            onValueChange = { slotQuantity = it.toIntOrNull() ?: 0 },
            label = { Text("Slot Quantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { getImage?.launch("image/*") }) {
            Text("Pick Image")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (image.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(image),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Open Registration Date",
            date = openRegistrationDate,
            onDateChanged = { newDate -> openRegistrationDate = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Close Registration Date",
            date = closeRegistrationDate,
            onDateChanged = { newDate -> closeRegistrationDate = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Cancellation Deadline",
            date = cancellationDeadline,
            onDateChanged = { newDate -> cancellationDeadline = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        DatePicker(
            title = "Start Date",
            date = startDate,
            onDateChanged = { newDate -> startDate = newDate }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = averageRating.toString(),
            onValueChange = { averageRating = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Average Rating") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Category Dropdown
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = categories.find { it.first == categoryId }?.second?.name ?: "Select Category",
                onValueChange = { },
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { (id, category) ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            categoryId = id
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (name.isNotBlank() && destination.isNotBlank()) {
                coroutineScope.launch {
                    FirestoreHelper.addTour(
                        Tour(
                            name = name,
                            openRegistrationDate = Timestamp(openRegistrationDate),
                            closeRegistrationDate = Timestamp(closeRegistrationDate),
                            cancellationDeadline = Timestamp(cancellationDeadline),
                            startDate = Timestamp(startDate),
                            destination = destination,
                            slotQuantity = slotQuantity,
                            image = image,
                            averageRating = averageRating,
                            categoryId = categoryId
                        )
                    )
                    navController.navigate("manage_tours")
                }
            }
        }) {
            Text("Add Tour")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewManageToursScreen() {
    TourBookingTheme {
        ManageToursScreen(navController = NavController(LocalContext.current))
    }
}
