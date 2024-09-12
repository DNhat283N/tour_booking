import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.project17.tourbooking.models.Account
import com.project17.tourbooking.utils.AuthState
import com.project17.tourbooking.utils.AuthViewModel
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

@Composable
fun ManageAccountsScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { avatarUri = it }
    }

    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    val authState by authViewModel.authState.observeAsState()

    fun resetForm() {
        username = ""
        email = ""
        password = ""
        role = ""
        avatarUri = null
        selectedAccount = null
    }

    // Update form when selectedAccount changes
    LaunchedEffect(selectedAccount) {
        selectedAccount?.let {
            username = it.username
            email = it.email
            password = it.password
            role = it.role
            avatarUri = it.avatar.takeIf { it.isNotEmpty() }?.let { Uri.parse(it) }
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.SignUpSuccess -> {
                Toast.makeText(context, "Create account successful!", Toast.LENGTH_SHORT).show()
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        accounts = FirestoreHelper.loadAccounts()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Account list
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(accounts) { account ->
                    AccountItem(account = account, onEdit = { selectedAccount = account })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation() // Hide password text
        )
        TextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Role") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Avatar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display selected avatar image
        avatarUri?.let {
            Image(painter = rememberImagePainter(it), contentDescription = "Avatar", modifier = Modifier.size(50.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                val avatarUrl = avatarUri?.let { FirestoreHelper.uploadImageToFirebase(it) }
                val accountToUpdate = Account(
                    username = username,
                    email = email,
                    password = if (password.isNotEmpty()) BCrypt.hashpw(password, BCrypt.gensalt()) else selectedAccount?.password ?: "",
                    avatar = avatarUrl ?: selectedAccount?.avatar ?: "",
                    role = role
                )

                if (selectedAccount != null) {
                    FirestoreHelper.updateAccount(selectedAccount!!.username, accountToUpdate)
                } else {
                    FirestoreHelper.addAccount(accountToUpdate)
                    authViewModel.signUp(email, password)
                }

                Toast.makeText(context, "Account saved successfully", Toast.LENGTH_SHORT).show()
                resetForm()
                accounts = FirestoreHelper.loadAccounts()
            }
        }) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            selectedAccount?.let {
                scope.launch {
                    FirestoreHelper.deleteAccount(it.email)
                    Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    // Clear form and refresh list
                    resetForm()
                    accounts = FirestoreHelper.loadAccounts()
                }
            }
        }) {
            Text("Delete")
        }
    }
}


@Composable
fun AccountItem(account: Account, onEdit: (Account) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(account.username, modifier = Modifier.weight(1f))
        Button(onClick = { onEdit(account) }) {
            Text("Edit")
        }
    }
}

