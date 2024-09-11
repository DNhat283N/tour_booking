package com.project17.tourbooking.activities.admin

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.project17.tourbooking.activities.admin.ui.theme.TourBookingTheme
import com.project17.tourbooking.models.Category
import kotlinx.coroutines.launch
import com.project17.tourbooking.ui.theme.Typography

@Composable
fun ManageCategoriesScreen(navController: NavController) {
    var categories by remember { mutableStateOf<List<Pair<String, Category>>>(emptyList()) }
    var isEditing by remember { mutableStateOf(false) }
    var editingCategoryId by remember { mutableStateOf<String?>(null) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // File picker launcher for image selection
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            coroutineScope.launch {
                val imageUrl = FirestoreHelper.uploadImageToFirebase(it)
                if (imageUrl != null && editingCategory != null) {
                    // Update the tempIcon state in EditingCategoryForm
                    editingCategory = editingCategory?.copy(icon = imageUrl)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        categories = FirestoreHelper.getCategories()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Manage Categories", style = Typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        if (isEditing) {
            EditingCategoryForm(
                category = editingCategory,
                onSave = { updatedCategory ->
                    if (editingCategoryId != null) {
                        coroutineScope.launch {
                            FirestoreHelper.updateCategory(editingCategoryId!!, updatedCategory)
                            isEditing = false
                            categories = FirestoreHelper.getCategories()
                            editingCategoryId = null
                            editingCategory = null
                        }
                    }
                },
                onCancel = {
                    isEditing = false
                    editingCategoryId = null
                    editingCategory = null
                },
                onPickImage = { launcher.launch("image/*") }
            )
        } else {
            NewCategoryForm {
                coroutineScope.launch {
                    val id = FirestoreHelper.addCategory(it)
                    categories = FirestoreHelper.getCategories()
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(categories) { (id, category) ->
                CategoryItem(
                    category = category,
                    onEdit = {
                        isEditing = true
                        editingCategoryId = id
                        editingCategory = category
                    },
                    onDelete = {
                        coroutineScope.launch {
                            FirestoreHelper.deleteCategory(id)
                            categories = FirestoreHelper.getCategories()
                        }
                    }
                )
            }
        }
    }
}



@Composable
fun NewCategoryForm(onAddCategory: (Category) -> Unit) {
    var name by remember { mutableStateOf("") }
    var tempIcon by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // File picker launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            coroutineScope.launch {
                val imageUrl = FirestoreHelper.uploadImageToFirebase(it)
                if (imageUrl != null) {
                    tempIcon = imageUrl
                }
            }
        }
    }

    Column {
        Text(text = "Add New Category", style = Typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick Image")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (tempIcon.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(tempIcon),
                contentDescription = "Selected Icon",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (name.isNotBlank() && tempIcon.isNotBlank()) {
                onAddCategory(Category(name = name, icon = tempIcon))
            } else {
                // Handle empty fields if needed
            }
        }) {
            Text("Add Category")
        }
    }
}

@Composable
fun EditingCategoryForm(
    category: Category?,
    onSave: (Category) -> Unit,
    onCancel: () -> Unit,
    onPickImage: () -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var newIcon by remember { mutableStateOf(category?.icon ?: "") }
    var tempIcon by remember { mutableStateOf(category?.icon ?: "") }

    Column {
        Text(text = "Edit Category", style = Typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.border(1.dp, Color.Gray).padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onPickImage) {
            Text("Pick Image")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (tempIcon.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(tempIcon),
                contentDescription = "Selected Category Icon",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = {
                if (category != null) {
                    onSave(category.copy(name = name, icon = newIcon))
                }
            }) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}


@Composable
fun CategoryItem(category: Category, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .border(1.dp, Color.Gray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (category.icon.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(category.icon),
                    contentDescription = "Category Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column {
                Text(text = category.name, style = Typography.bodyMedium)
            }
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TourBookingTheme {
        ManageCategoriesScreen(navController = NavController(context = LocalContext.current))
    }
}