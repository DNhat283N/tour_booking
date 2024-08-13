package com.project17.tourbooking.activities.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project17.tourbooking.models.Account
import com.project17.tourbooking.utils.FirestoreHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var editingAccount by remember { mutableStateOf<Account?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Listen for real-time updates
    LaunchedEffect(Unit) {
        FirestoreHelper.getAllAccountsSnapshotListener { updatedAccounts ->
            accounts = updatedAccounts
        }
    }

    fun showEditDialog(account: Account? = null) {
        editingAccount = account
        selectedImageUri = if (account != null && account.avatar.isNotEmpty()) Uri.parse(account.avatar) else null
        username = account?.username ?: ""
        email = account?.email ?: ""
        password = account?.password ?: ""
        showDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Account Management") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showEditDialog() }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Account")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(accounts) { account ->
                AccountItem(
                    account = account,
                    onEdit = { showEditDialog(account) },
                    onDelete = {
                        editingAccount = account
                        showDeleteConfirmation = true
                    }
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingAccount == null) "Add Account" else "Edit Account") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") }
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") }
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation()
                        )

                        Button(onClick = { launcher.launch("image/*") }) {
                            Text("Select Avatar")
                        }
                        selectedImageUri?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = "Selected Avatar",
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (!emailRegex.matches(email)) {
                                println("Invalid email format")
                                return@Button
                            }

                            coroutineScope.launch {
                                try {
                                    val avatarUrl = selectedImageUri?.let {
                                        FirestoreHelper.uploadImageToFirebaseStorage(it)
                                    } ?: editingAccount?.avatar

                                    val newAccount = Account(username, email, password, avatarUrl ?: "")
                                    if (editingAccount == null) {
                                        FirestoreHelper.addAccount(newAccount)
                                    } else {
                                        val success = FirestoreHelper.updateAccount(editingAccount!!.username, newAccount)
                                        if (!success) {
                                            println("Failed to update account")
                                        }
                                    }
                                } catch (e: Exception) {
                                    println("Error saving account: ${e.message}")
                                } finally {
                                    showDialog = false
                                    editingAccount = null
                                    selectedImageUri = null
                                    username = ""
                                    email = ""
                                    password = ""
                                }
                            }
                        }
                    ) {
                        Text(if (editingAccount == null) "Add" else "Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete this account?") },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    editingAccount?.let {
                                        val success = FirestoreHelper.deleteAccount(it.username)
                                        if (!success) {
                                            println("Failed to delete account")
                                        }
                                    }
                                } catch (e: Exception) {
                                    println("Error deleting account: ${e.message}")
                                } finally {
                                    showDeleteConfirmation = false
                                    editingAccount = null
                                }
                            }
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun AccountItem(account: Account, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = account.avatar.takeIf { it.isNotEmpty() } ?: "default_avatar_url", // Replace with your default avatar URL
            contentDescription = "Avatar",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Username: ${account.username}")
            Text(text = "Email: ${account.email}")
        }
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = Color.Black
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.Black
            )
        }
    }
}
