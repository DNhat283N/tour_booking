package com.project17.tourbooking.activities.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project17.tourbooking.models.Category
import com.project17.tourbooking.utils.FirestoreHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen() {
    var categories by remember { mutableStateOf<Map<String, Category>>(emptyMap()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingCategoryId by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Set up real-time listener for categories
    LaunchedEffect(Unit) {
        FirestoreHelper.getAllCategoriesSnapshotListener { updatedCategories ->
            categories = updatedCategories
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Category Management") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingCategoryId = null
                showDialog = true
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(categories.entries.toList()) { entry ->
                CategoryItem(
                    categoryId = entry.key,
                    category = entry.value,
                    onEdit = { id ->
                        editingCategoryId = id
                        showDialog = true
                    },
                    onDelete = { id ->
                        coroutineScope.launch {
                            try {
                                if (FirestoreHelper.deleteCategory(id)) {
                                    // No need to update categories map manually; listener will handle it
                                }
                            } catch (e: Exception) {
                                // Handle exceptions here
                                println("Error deleting category: ${e.message}")
                            }
                        }
                    }
                )
            }
        }
    }

    if (showDialog) {
        CategoryDialog(
            editingCategoryId = editingCategoryId,
            categories = categories,
            onSave = { id, category ->
                coroutineScope.launch {
                    try {
                        if (id == null) {
                            // Add new category
                            val newId = FirestoreHelper.addCategory(category)
                            if (newId != null) {
                                // No need to update categories map manually; listener will handle it
                            }
                        } else {
                            // Update existing category
                            if (FirestoreHelper.updateCategory(id, category)) {
                                // No need to update categories map manually; listener will handle it
                            }
                        }
                    } catch (e: Exception) {
                        // Handle exceptions here
                        println("Error saving category: ${e.message}")
                    } finally {
                        // Close the dialog and reset editingCategoryId regardless of success or failure
                        showDialog = false
                        editingCategoryId = null
                    }
                }
            },
            onCancel = {
                showDialog = false
                editingCategoryId = null
            }
        )
    }
}

@Composable
fun CategoryItem(categoryId: String, category: Category, onEdit: (String) -> Unit, onDelete: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category.name, modifier = Modifier.weight(1f))
        IconButton(onClick = { onEdit(categoryId) }) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = Color.Black
            )
        }
        IconButton(onClick = { onDelete(categoryId) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun CategoryDialog(
    editingCategoryId: String?,
    categories: Map<String, Category>,
    onSave: (String?, Category) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember {
        mutableStateOf(editingCategoryId?.let { categories[it]?.name } ?: "")
    }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(if (editingCategoryId == null) "Add Category" else "Edit Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(editingCategoryId, Category(name)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
