import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.project17.tourbooking.models.Account

@Composable
fun ManageAccountsScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }

    // Load accounts on start
    LaunchedEffect(Unit) {
        accounts = FirestoreHelper.getAllAccounts()
    }

    // Update fields when a new account is selected
    LaunchedEffect(selectedAccount) {
        selectedAccount?.let {
            username = it.username
            email = it.email
            password = ""  // Clear password field for security reasons
            role = it.role
            avatarUri = Uri.parse(it.avatar)
            imageBitmap = it.avatar.let { url ->
                try {
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(url))
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap?.asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Accounts",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Avatar Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            imageBitmap?.let {
                Image(bitmap = it, contentDescription = "Avatar")
            } ?: Icon(imageVector = Icons.Default.Person, contentDescription = "Placeholder")
        }

        // Pick Image Button
        ImagePicker { uri ->
            avatarUri = uri
            imageBitmap = uri?.let { uriToBitmap(context, it) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Role") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                coroutineScope.launch {
                    val imageUrl = avatarUri?.let { uri ->
                        FirestoreHelper.uploadImageToFirebase(uri)
                    }
                    val account = Account(
                        username = username,
                        email = email,
                        password = password,
                        avatar = imageUrl ?: "",
                        role = role
                    )
                    if (selectedAccount == null) {
                        FirestoreHelper.addAccount(account)
                    } else {
                        FirestoreHelper.updateAccount(account)
                    }
                    accounts = FirestoreHelper.getAllAccounts()  // Refresh list
                }
            }) {
                Text(if (selectedAccount == null) "Add Account" else "Update Account")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                selectedAccount?.let {
                    coroutineScope.launch {
                        FirestoreHelper.deleteAccount(it.username)
                        accounts = FirestoreHelper.getAllAccounts()  // Refresh list
                        selectedAccount = null
                        username = ""
                        email = ""
                        password = ""
                        role = ""
                        avatarUri = null
                        imageBitmap = null
                    }
                }
            }) {
                Text("Delete Account")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Accounts List
        LazyColumn {
            items(accounts) { account ->
                AccountItem(account = account, onAccountSelected = { selectedAccount = it })
            }
        }
    }
}

@Composable
fun ImagePicker(onImagePicked: (Uri?) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        onImagePicked(uri)
    }
    Button(onClick = { launcher.launch("image/*") }) {
        Text("Pick Image")
    }
}

@Composable
fun AccountItem(account: Account, onAccountSelected: (Account) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onAccountSelected(account) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = account.username,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Edit",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

fun uriToBitmap(context: Context, uri: Uri): ImageBitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
